class QuackerService {

    boolean transactional = true

    static exposes = ['jms']
    static destination = "queue.quacker"

    def quackerCache

    def onMessage( status ) {

        def currentQuacker = status.quacker

        def following = currentQuacker.withCriteria {

            projections { property "username" }
            following { eq'username', currentQuacker.username }
        }

        for( username in following ) {
            println "DEBUG: removing from cache onMessage: ${username}"
            quackerCache.remove( username )
        }

        println "just received a message: '$status.message' from $status.quacker.userRealName"
    }
}

