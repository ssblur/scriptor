package com.ssblur.scriptor.error

class WordNotFoundException(key: String) : RuntimeException("A word with key \"$key\" was not found.")
