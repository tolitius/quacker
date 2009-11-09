<div id="message${status.id}" class="quack" style="border 1px solid gray; padding:10px 10px 15px 10px;">

  <strong>
    ${status.quacker.userRealName} quacked...
  </strong>

  <div>
    ${status.message} |
    <g:remoteLink controller="status"
                    action="likeIt"
                    update="liked_${status.id}"
                    id="${status.id}">Like</g:remoteLink>
  </div>

  <div id="liked_${status.id}" style="float:left;padding-bottom:10px;color:#BBB;font-style:oblique;">
    <g:if test="${status.liked}">
      Liked by:
      <g:each in="${status}">
        ${it.liked.userRealName}
      </g:each>
    </g:if>
  </div>


</div>

