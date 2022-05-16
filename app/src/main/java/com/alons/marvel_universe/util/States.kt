package com.alons.marvel_universe.util


sealed class States<T>(val data :T?=null,val message:String?=null){
    class Loading<T>(data:T?=null):States<T>(data)
    class Success<T>(data:T):States<T>(data)
    class Error<T>(message:String,data:T?=null):States<T>(data,message)
}