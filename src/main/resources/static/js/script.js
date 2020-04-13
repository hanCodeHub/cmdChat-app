'use strict'

let stompClient
let username

// Configuration in stomp-js documentation: 
// https://stomp-js.github.io/stomp-websocket/codo/extra/docs-src/Usage.md.html

const connect = (event) => {
    username = document.querySelector('#username').value.trim()

    if (username) {
        // creates WebSocket client and connects to STOMP server
        stompClient = Stomp.over(new SockJS('/ws')) 
        // connect(headers, connectCallback, errorCallback)
        stompClient.connect({}, onConnect, onError)
    }
    event.preventDefault()
}


const onConnect = () => {

    let channel = 'abc'

    // client subscribes to the given channel on successful connection
    stompClient.subscribe(`/topic/public/${channel}`, onMessageReceived)
    // client sends message to app to broadcast new user to the given channel
    stompClient.send(`/app/chat.newUser/public/${channel}`,
        {},
        JSON.stringify({sender: username, state: 'CONNECT'})
    )
    const status = document.querySelector('#status')
    status.className = 'hide'
}


const disconnect = () => {
    // client disconnects from the STOMP server
    // onDisconnect called only if client was connected
    stompClient.disconnect(onDisconnect, {})
}


const onDisconnect = () => {
    // calls onMessageReceived manually because connection to server is cut
    const disconnectMessage = {
        state: "DISCONNECT",
        content: null,
        sender: username,
        time: null
    }
    const payload = {
        body: JSON.stringify(disconnectMessage)
    }

    // disconnect message will only be rendered for the current user
    onMessageReceived(payload)
}


const onError = (error) => {
    const status = document.querySelector('#status')
    status.innerHTML = error.headers.message
}


const sendMessage = (event) => {
    const messageInput = document.querySelector('#message')
    const messageContent = messageInput.value.trim()

    let channel = 'abc'

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            content: messageInput.value,
            state: 'CHAT',
            time: moment().calendar()
        }
        // client broadcasts message to channel
        stompClient.send(`/app/chat.send/public/${channel}`, {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }
    event.preventDefault();
}


const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);

    const chatCard = document.createElement('div')
    chatCard.className = 'card-body'

    const flexBox = document.createElement('div')
    flexBox.className = 'd-flex justify-content-end mb-4'
    chatCard.appendChild(flexBox)

    const messageElement = document.createElement('div')
    messageElement.className = 'msg_container_send'

    flexBox.appendChild(messageElement)

    if (message.state === 'CONNECT') {

        message.content = message.sender + ' connected!'
    } else if (message.state === 'DISCONNECT') {

        message.content = message.sender + ' left!'
    } else {


        const avatarContainer = document.createElement('div')
        avatarContainer.className = 'img_cont_msg'
        const avatarElement = document.createElement('div')
        avatarElement.className = 'circle user_img_msg'
        const avatarText = document.createTextNode(message.sender[0])
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender)
        avatarContainer.appendChild(avatarElement)

        messageElement.style['background-color'] = getAvatarColor(message.sender)

        flexBox.appendChild(avatarContainer)

        const time = document.createElement('span')
        time.className = 'msg_time_send'
        time.innerHTML = message.time
        messageElement.appendChild(time)

    }

    messageElement.innerHTML = message.content

    const chat = document.querySelector('#chat')
    chat.appendChild(flexBox)
    chat.scrollTop = chat.scrollHeight
}

const hashCode = (str) => {
    let hash = 0
    for (let i = 0; i < str.length; i++) {
       hash = str.charCodeAt(i) + ((hash << 5) - hash)
    }
    return hash
}


const getAvatarColor = (messageSender) => {
    const colours = ['#2196F3', '#32c787', '#1BC6B4', '#A1B4C4']
    const index = Math.abs(hashCode(messageSender) % colours.length)
    return colours[index]
}

const loginForm = document.querySelector('#login-form')
loginForm.addEventListener('submit', connect)
const messageControls = document.querySelector('#message-controls')
messageControls.addEventListener('submit', sendMessage)
const logoutBtn = document.querySelector('#logout-btn')
logoutBtn.addEventListener('click', disconnect)