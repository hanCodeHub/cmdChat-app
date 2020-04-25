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
    }
}


// logged out state
class LoggedOutState extends ChatPageState {
    constructor(stateMachine, connectionVars, domVars) {
            super(stateMachine, connectionVars, domVars)
    }

    login() {
        console.log("logging in")
    }

    logout() {
        console.log("cannot logout again")
        return null
    }

    joinChannel() {
        console.log('joining channel')
    }

    leaveChannel() {
        console.log("leaving channel")
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