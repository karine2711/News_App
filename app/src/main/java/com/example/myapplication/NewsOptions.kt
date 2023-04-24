package com.example.myapplication


// I realize this is bad practice in general.
// But I think keeping a global object in a small application like this
// will not hurt, on the contrary, it eliminates a lot of the complexity
object NewsOptions {
    var category: String? = null
    var query: String? = null
}
