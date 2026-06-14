package com.example.e_vidya

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonLoader {

    fun loadQuestions(
        context: Context,
        fileName: String
    ): List<Question> {

        val json =
            context.assets
                .open(fileName)
                .bufferedReader()
                .use {
                    it.readText()
                }

        val type =
            object :
                TypeToken<List<Question>>() {}.type

        return Gson().fromJson(
            json,
            type
        )
    }
}