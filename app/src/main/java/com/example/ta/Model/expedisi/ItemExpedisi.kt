package com.example.ta.Model.expedisi

/**
 * Created by Robby Dianputra on 2/15/2018.
 */

public class ItemExpedisi {

    var id: String? = null
    var name: String? = null

    constructor() {}

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }
}
