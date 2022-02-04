package com.example.ktsignupapp

class profileinfo {
    var userName: String? = null
    var userNumber: String? = null
    var userEmail: String? = null
    var userid: String? = null
    var password: String? = null
    var imageURL: String? = null

    constructor() {}
    constructor(
        userName: String?,
        userNumber: String?,
        userEmail: String?,
        userid: String?,
        password: String?,
        url: String?
    ) {
        this.userName = userName
        this.userNumber = userNumber
        this.userEmail = userEmail
        this.userid = userid
        this.password = password
        imageURL = url
    }
}