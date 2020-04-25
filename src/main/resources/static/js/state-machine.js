'use strict'

// State machine for the main chatpage. Controls state of connections and DOM elements.
// param connectionVars: object literal of connection variables
// param domVars: object literal of dom elements
class ChatPageStateMachine {
    constructor(connectionVars, domVars) {
        // Each state holds a reference to this ChatPage in order to changeState()
        this.loggedInState = new LoggedInState(this, connectionVars, domVars)
        this.loggedOutState = new LoggedOutState(this, connectionVars, domVars)
        this.chattingState = new ChattingState(this, connectionVars, domVars)
        // current state starts before user logs in
        this.state = this.loggedOutState
    }

    // allows the changing of current state by state objects
    changeState(newState) {
        // checks if newState is valid
        if (this.loggedInState !== newState || 
            this.loggedOutState !== newState || 
            this.chattingState !== newState) {
                throw "Unsupported operation: cannot change to a state that does not exist."
            }
        this.state = newState
    }

    // user logs in
    login() {
        this.state.login()
    }

    // user logs out
    logout() {
        this.state.logout()
    }

    // user joins a channel
    joinChannel() {
        this.state.joinChannel()
    }

    // user leaves a channel
    leaveChannel() {
        this.state.leaveChannel()
    }
}

