'use strict'

let stompClient
let username

// Configuration of stompJs documentation: 
// https://stomp-js.github.io/stomp-websocket/codo/extra/docs-src/Usage.md.html


// event handler for when the user signs in
const connect = (event) => {
    username = document.querySelector('#username').value.trim()

    if (username && !stompClient) {
        // creates WebSocket client and connects to STOMP server with SockJS
        stompClient = Stomp.over(new SockJS('/ws'))
        // connect(headers, connectCallback, errorCallback)
        stompClient.connect({}, onConnect, onError)
    }
    event.preventDefault()
}


// callback function upon successful connection to the STOMP server
const onConnect = () => {
    // single channel - dynamic when multichannels are available in future release 
    let channel = 'abc'

    // client subscribes to the given channel on successful connection
    // subscribe(destination, callback every time something is broadcasted to destination)
    stompClient.subscribe(`/topic/public/${channel}`, renderMessage)

    // client sends message to app to broadcast new user to the given channel
    stompClient.send(`/app/chat.newUser/public/${channel}`,
        {},
        JSON.stringify({ sender: username, state: 'CONNECT' })
    )
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
    username = null;
    stompClient = null;

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
    event.preventDefault();
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
        message.content = message.sender + ' connected!'
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


// checks whether user is authenticated by pinging endpoint ping-user
const checkUser = async () => {
    try {
        await fetch("http://localhost:8080/ping-user")
            .then(res => res.json())
            .then(userData => onUserLoggedIn(userData))
            .catch(err => console.log(err))
    } catch (err) {
        console.log(err)
    }
}


// Edits the page if user is logged in by fetching name first
const onUserLoggedIn = (userData) => {
    const bannerText = document.querySelector('#banner-text')
    if (userData.name) {
        try {
            fetch("http://localhost:8080/user")
                .then(res => res.json())
                .then(user => {
                    bannerText.innerHTML = user.name
                    username = user.name;
                })
                .catch(err => console.log(err))
        } catch (err) {
            console.log(err)
        }
    }
    else {
        bannerText.innerHTML = "Sign in with GitHub and start chatting!"
    }
}


// EVENT LISTENERS
// login
const loginForm = document.querySelector('#login-form')
loginForm.addEventListener('submit', connect)

// login with GitHub
const loginBtn = document.querySelector('#login-btn')
loginBtn.addEventListener('click', login)

// send message
const messageControls = document.querySelector('#message-controls')
messageControls.addEventListener('submit', sendMessage)

// logout
const logoutBtn = document.querySelector('#logout-btn')
logoutBtn.addEventListener('click', disconnect)

checkUser();