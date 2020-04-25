'use strict'

// holds variables for connections and DOM elements.
// to be extended by concrete states
class ChatPageState {
    constructor(stateMachine, connectionVars, domVars) {
        this.stateMachine = stateMachine

        // unpacks properties for child states
        this.stompClient = connectionVars.stompClient
        this.username = connectionVars.username
        this.subscription = connectionVars.subscription
        this.loginBtn = domVars.loginBtn
        this.logoutBtn = domVars.logoutBtn
        this.messageControls = domVars.messageControls
        this.joinBtn = domVars.joinBtn
        this.leaveBtn = domVars.leaveBtn
        this.bannerText = domVars.bannerText
        this.chatSection = domVars.chatSection
    }

}


// logged out state
class LoggedOutState extends ChatPageState {
    constructor(stateMachine, connectionVars, domVars) {
        super(stateMachine, connectionVars, domVars)
    }

    login() {
        // pings the server whenever homepage loads to see if user logged in
        fetch("/user/ping")
            .then(res => res.text())
            .then(name => {
                if (name) {  // obtains full name and username from secured endpoint
                    fetch("/user")
                        .then(res => res.json())
                        .then(user => {  // user is in logged in state
                            this.bannerText.innerHTML = `Hello ${user.name}! Your chat name is 
                                    ${user.username}.`
                            this.username = user.username; // username is OAuth2 client username
                            // hides login button. shows logout button and chat section.
                            this.loginBtn.classList.add("hide-important")
                            this.logoutBtn.classList.remove("hide-important")
                            this.chatSection.classList.remove("hide-important")
                        })
                        .catch(err => console.log(err))
                }
                else {  // default text for when user not logged in
                    this.bannerText.innerHTML = "Sign in and start chatting!"
                }
            })
            .catch(err => console.log(err))

    }

    logout() {
        throw 'Unsupported operation: cannot logout again'
    }

    joinChannel() {
        throw 'Unsupported operation: cannot join channel before logging in'
    }

    leaveChannel() {
        throw 'Unsupported operation: cannot leave channel before logging in'
    }
}


// logged in state
class LoggedInState extends ChatPageState {
    constructor(stateMachine, connectionVars, domVars) {
        super(stateMachine, connectionVars, domVars)
    }

    login() {

    }

    logout() {
        stompClient = Stomp.over(new SockJS('/ws'))
        stompClient.connect({},
            () => {
                // onConnect callback
                console.log(`${username} has connected to STOMP server`)
            }, () => {
                // onError callback
                throw 'Connection to STOMP server failed'
            })
    }

    joinChannel() {

    }

    leaveChannel() {

    }
}


// chatting state
class ChattingState extends ChatPageState {
    constructor(stateMachine, connectionVars, domVars) {
        super(stateMachine, connectionVars, domVars)
    }

    login() {

    }

    logout() {

    }

    joinChannel() {

    }

    leaveChannel() {

    }
}