'use strict'

let stompClient  // assigned when client connects to STOMP server
let username  // assigned when user signs in
let subscription  // the channel object that user is subscribed to

// Configuration of stompJs documentation: 
// https://stomp-js.github.io/stomp-websocket/codo/extra/docs-src/Usage.md.html


// event handler for when the user signs in
const connect = () => {

    if (username && !stompClient) {
        // creates WebSocket client and connects to STOMP server with SockJS
        stompClient = Stomp.over(new SockJS('/ws'))
        // connect(headers, connectCallback, errorCallback)
        stompClient.connect({}, onConnect, onError)
    }
}


// callback function upon successful connection to the STOMP server
const onConnect = () => {
    console.log(`${username} has connected to STOMP server`)
}


// subscribes user to a given channel
const subscribe = (event) => {
    event.preventDefault()
    if (subscription) return; // if user already subscribed, skip this for now

    // hardcoded - CHANGE to dynamic when multichannels are available in future release 
    const channel = 'abc'

    // subscribe(destination, callback every time something is broadcasted to destination)
    subscription = stompClient.subscribe(`/topic/public/${channel}`, renderMessage)

    // client sends message to app to broadcast new user to the given channel
    stompClient.send(`/app/chat.newUser/public/${channel}`,
        {},
        JSON.stringify({ sender: username, state: 'CONNECT' })
    )
}


// unsubscribes from given channel
const unsubscribe = () => {
    if (!subscription) return;

    subscription.unsubscribe();
    subscription = null

    // calls renderMessage manually because connection to server is cut
    const disconnectMessage = {
        state: "DISCONNECT",
        sender: username,
    }
    const payload = {
        body: JSON.stringify(disconnectMessage)
    }
    renderMessage(payload)
}


// event handler for when user disconnects/logout
const disconnect = () => {
    // client disconnects from the STOMP server
    // onDisconnect called only if client was connected
    if (stompClient && username) {
        stompClient.disconnect(onDisconnect, {})
    }
}


// callback function upon successful disconnect from the STOMP server
const onDisconnect = () => {
    // calls renderMessage manually because connection to server is cut
    const disconnectMessage = {
        state: "DISCONNECT",
        sender: username,
    }
    const payload = {
        body: JSON.stringify(disconnectMessage)
    }
    // resets username and STOMP client
    stompClient = null;
    subscription = null;

    // disconnect message will only be rendered for the current user
    renderMessage(payload)
}


// callback function to display an error message to the status section
const onError = (error) => {
    const status = document.querySelector('#status')
    status.hidden = false;
    status.innerHTML = error.headers.message
}


// event handler for sending message to a destination in the STOMP server
const sendMessage = (event) => {
    event.preventDefault();
    if (!subscription) return;

    const messageInput = document.querySelector('#message')
    const messageContent = messageInput.value.trim()

    let channel = 'abc'  // hard coded channel for now

    // constructs the message object
    if (messageContent && stompClient && username) {
        const chatMessage = {
            sender: username,
            content: messageInput.value,
            state: 'CHAT'
        }
        // client broadcasts message to channel and resets input
        stompClient.send(`/app/chat.send/public/${channel}`, {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }
}


// renders a message to the chat section
const renderMessage = (payload) => {
    const message = JSON.parse(payload.body);

    // creates message element within a flexbox container
    const msgContainer = document.createElement('div')
    msgContainer.className = 'justify-content-start mb-4'
    const msgElement = document.createElement('p')

    // sets message content based on its state
    if (message.state === 'CONNECT') {
        message.content = message.sender + ' joined!'
    } else if (message.state === 'DISCONNECT') {
        message.content = message.sender + ' left!'
    } else {
        // displays details about sender name and time on top of message
        const detailContainer = document.createElement('div')
        const senderName = document.createElement('span')
        const messageTime = document.createElement('span')
        senderName.className = "h6 font-weight-bold mr-2"
        messageTime.className = "badge badge-light align-middle"
        senderName.innerHTML = message.sender

        // constructs date and time
        if (message.dateTime) {
            const dt = new Date(message.dateTime)
            const nowDate = dt.toDateString().substring(4, 10)
            const nowTime = dt.toLocaleTimeString()
            messageTime.innerHTML = `${nowDate} - ${nowTime}`
        }

        detailContainer.appendChild(senderName)
        detailContainer.appendChild(messageTime)
        msgContainer.appendChild(detailContainer)
    }

    // populates message and adds to message container
    msgElement.innerHTML = message.content
    msgContainer.appendChild(msgElement)

    // adds message container to chat
    const chat = document.querySelector('#chat')
    chat.appendChild(msgContainer)
    scrollBottom()  // keep view scrolled to the bottom
}


// Keeps the chat window scrolled to the bottom of messages
const scrollBottom = () => {
    const chatBody = document.getElementById("chat-body")
    chatBody.scroll(0, chatBody.scrollHeight)
}


// logs the user in via connected OAuth2 client
const login = () => {
    location.href = '/oauth2/authorization/github'
}


// logs the user out of the OAuth2 authentication
const logout = async () => {
    try {
        await fetch("/logout")
            .then(res => res.text())
            .then(text => onUserLoggedOut())
            .catch(err => console.log(err))
    } catch (err) {
        console.log(err)
    }
}


// checks whether user is authenticated by pinging public endpoint /ping-user
// checkUser() is invoked each time homepage loads
const checkUser = async () => {
    try {
        await fetch("/ping-user")
            .then(res => res.text())
            .then(name => onUserAuth(name))
            .catch(err => console.log(err))
    } catch (err) {
        console.log(err)
    }
}


// handler for when user is authenticated with a given name
const onUserAuth = async (name) => {
    const bannerText = document.querySelector('#banner-text')
    // if user exists, fetches all user data from secured endpoint /user
    if (name) {
        try {
            await fetch("/user")
                .then(res => res.json())
                .then(user => {
                    bannerText.innerHTML = `Hello ${user.name}! Your chat name is ${user.username}.`
                    username = user.username; // username is OAuth2 client username
                    // connects to STOMP server and registers user in DB
                    connect()

                })
                .catch(err => console.log(err))
        } catch (err) {
            console.log(err)
        }
    }
    else {
        bannerText.innerHTML = "Sign in and start chatting!"
    }
}


// saves a user to the database via POST request to /user
const registerUser = async (name) => {
    const payload = {
        name: name
    }
    try {
        await fetch('/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(user => user)
            .catch(err => console.log(err))
    } catch (error) {
        console.log(error)
    }

}


// handler for when user is logged out
const onUserLoggedOut = () => {
    const bannerText = document.querySelector('#banner-text')
    bannerText.innerHTML = "Sign in and start chatting!"
    // clear username and disconnect from STOMP server
    username = null
    disconnect()
}


// EVENT LISTENERS
// join a channel
const joinForm = document.querySelector('#join-form')
joinForm.addEventListener('submit', subscribe)

// login with GitHub
const loginBtn = document.querySelector('#login-btn')
loginBtn.addEventListener('click', login)

// send message
const messageControls = document.querySelector('#message-controls')
messageControls.addEventListener('submit', sendMessage)

// logout
const leaveBtn = document.querySelector('#leave-btn')
leaveBtn.addEventListener('click', unsubscribe)

// if username is null check if user is  signed in
if (!username) {
    checkUser();
}
