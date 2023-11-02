package com.rizfan.storyapp

import com.rizfan.storyapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "https://picsum.photos/seed/picsum/200/300",
                "Title $i",
                "Description $i",
                0.0,
                "id $i",
                0.0
            )
            items.add(story)
        }
        return items
    }
}