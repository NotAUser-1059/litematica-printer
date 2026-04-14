plugins {
    base
}

description = "Aggregator project. The latest and prioritized implementation lives in :v1_21"

tasks.named("build") {
    dependsOn(":v1_21:build")
}
