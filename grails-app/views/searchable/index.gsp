<html>
  <head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="layout" content="main">
    <g:javascript library="prototype" />

    <g:javascript>
        function toggleShowHideLink( quackerId ) {
            $( 'messages_' + quackerId ).toggle();
            $( 'show_quacks_link_' + quackerId ).toggle();
            $( 'hide_quacks_link_' + quackerId ).toggle();
        }
    </g:javascript>

    <title>search results</title>

  </head>

    <body id="index">

        <h1 id="search_results">search results</h1>

        <g:each var="quacker" in="${searchResult?.results}">

            <div id="search_result">

                <div style="float:left;">${quacker.userRealName}</div>

                <div id="show_quacks_link_${quacker.id}" style="float:left;">

                    <g:remoteLink controller="searchable"
                                    action="showUserQuacks"
                                    update="messages_${quacker.id}"
                                    onSuccess="toggleShowHideLink( ${quacker.id} );"
                                    id="${quacker.id}">show quacks</g:remoteLink>
                </div>

                <div id="hide_quacks_link_${quacker.id}" style="display:none; float:left;">
                    <g:remoteLink onSuccess="toggleShowHideLink( ${quacker.id} );">
                                    hide quacks</g:remoteLink>
                </div>

                <div style="float:left;">|</div>
                <div style="float:left;">
                    <g:link controller="status" action="follow" id="${quacker.id}">follow</g:link>
                </div>
                <div id="messages_${quacker.id}"
                     class="search_quacks"
                     style="display:none;"></div>

            </div>
        </g:each>

    </body>
</html>

