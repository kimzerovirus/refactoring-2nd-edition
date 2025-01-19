package me.kzv.refactoring.chapter01

import kotlinx.serialization.json.Json

private const val playsJson = """
    {
      "hamlet": {"name": "Hamlet", "type": "tragedy"},
      "as-like": {"name": "As You Like It", "type": "comedy"},
      "othello": {"name": "Othello", "type": "tragedy"}
    }
"""

private const val invoicesJson = """
    [
      {
        "customer": "BigCo",
        "performances": [
          {
            "playID": "hamlet",
            "audience": 55
          },
          {
            "playID": "as-like",
            "audience": 35
          },
          {
            "playID": "othello",
            "audience": 40
          }
        ]
      }
    ]
"""

private val json = Json { ignoreUnknownKeys = true }

val plays = json.decodeFromString<Map<String, Play>>(playsJson)
val invoices = json.decodeFromString<List<Invoice>>(invoicesJson)