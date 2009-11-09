import net.sf.ehcache.Element
import grails.converters.*

class StatusController {

    // autowired bean form resources.groovy
    def quackerCache

    def index = {

        def messages = quackerCache.get(principalInfo.username)?.value

        if (!messages) {
            messages = findStatusMessages()
            quackerCache.put new Element(principalInfo.username, messages)
        }

        def feedOutput = {
            title = "Quacker RSS feed"
            description = "Quacker RSS feed"

            link = g.link( controller:"status", absolute:true )

            for ( s in messages ) {

                entry( "${s.quacker.userRealName} posted:" ) {
                    link = g.link( controller:"status", absolute:true )
                    s.message
                }
            }
        }

        withFormat {

            html ([messages:messages])

            xml {
                render messages as XML
            }
            rss {
                render ( feedType:"rss", feedVersion:"2.0", feedOutput )
            }
        }
    }

    def update = {

        def status = new Status( params )
        status.quacker = lookUpQuacker()
        status.save()

        quackerCache.remove(principalInfo.username)

        render template:"message", var:"status", collection:findStatusMessages()
    }

    def refreshQuacks = {
        // in case somebody left his browser open, but his session is expired,
        // 'principalInfo' would be null
        if( principalInfo.metaClass.hasProperty(principalInfo, "username") ) {

            def messages = quackerCache.get(principalInfo.username)?.value

            if (!messages) {
                messages = findStatusMessages()
                quackerCache.put new Element(principalInfo.username, messages)
            }

            render template:"message", var:"status", collection:messages
        }
    }

    def follow = {

        def q = User.get(params.id)

        if(q) {

            def current = lookUpQuacker()
            current.addToFollowing( q )
            current.save()
        }

        redirect action:"index"
    }

    def likeIt = {

        def status = Status.get(params.id)

        if( status ) {

            def current = lookUpQuacker()
            status.addToLiked( current )
            status.save()

            println "DEBUG: ${current.userRealName} liked '${status.message}' quack"
        }

        // to display "likes" on an auto refresh for:
        def refreshList = []
        // everybody who follows the owner of the quack
        refreshList << status.quacker.following.collect{ it.username }
        // the owner of the quack
        refreshList << status.quacker.username
        // this particular quacker in case s/he liked the quack, but does not follow
        refreshList << principalInfo.username

        println "DEBUG: clearing cache on 'like' for -> ${refreshList.flatten()}"
        refreshList.flatten().each{ quackerCache.remove(it) }

        String likedByUsers = status.liked.collect { it.userRealName }

        println "DEBUG: rendering -> 'Liked by: ${likedByUsers}'"
        render "Liked by: ${likedByUsers}"
    }

    private lookUpQuacker() {
        User.findByUsername( principalInfo.username )
    }

    private findStatusMessages() {
        def qer = lookUpQuacker()

        def messages = Status.withCriteria {
          or {
            quacker {
              eq('username', qer.username)
            }
            if (qer.following) {
              inList("quacker", qer.following)
            }
          }
          maxResults 10
          order 'dateCreated', 'desc'
        }

        return messages
    }
}

