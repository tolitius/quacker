// Place your Spring DSL code here
beans = {

    quackerCache(org.springframework.cache.ehcache.EhCacheFactoryBean) {
        timeToLive = 1200
    }

}

