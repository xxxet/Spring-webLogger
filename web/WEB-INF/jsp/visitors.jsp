<%-- 
    Document   : welcomePage
    Created on : 24.11.2012, 0:45:18
    Author     : zzz

${visitors[fn:length(welcomeMessage)-1][0]}
--%>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <LINK href="resources/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>

        <div id="main">
            <div id="left_shad"></div>
            <div id="right_shad">
                <div id="right_shad2">
                </div>
            </div>

            <div id="header">

                <span id="title">Congratulations, you have been logged!</span>
                <!--    <span id="tagline">By the way, partyvans on route: 8432</span> -->
            </div>

            <div id="content">
                <div>
                    <div>
                        <p >Hi, your ip is: ${currentVisitor.ip}  
                            [<a href="http://toolserver.org/~overlordq/cgi-bin/whois.cgi?lookup=${currentVisitor.ip}">who.is</a>] 
                        </p>
                        <c:if test="${wasHere}">
                            <p > Last time you was here: ${currentVisitor.date}</p>
                        </c:if> 

                        <c:if test="${currentVisitor.post!=null}">
                            <p>${currentVisitor.post}</p>
                        </c:if>  

                      <p >
                            AGENT: ${currentVisitor.userAgent} 
                      </p>  

                            <div id="formholder">
                                <form method="post" name="message_form">
                                    Say something:
                                    <input type="text" name="post" id="post" maxlength="255"/>
                                    <input type="submit"/>
                                </form>
                            </div>
                    </div>


                    <c:if test="${visitorsList!=null}">
                         <div>
                        <p>Before you, here were some people:</p>
                        </div>
                    </c:if>	

                    <c:forEach items="${visitorsList}" var="visitor">
                        <div>
                            <h3>
                                <span class="ip"> IP: ${visitor.ip}</span>  [<a href="http://toolserver.org/~overlordq/cgi-bin/whois.cgi?lookup=${visitor.ip}" >who.is</a>] ||| ${visitor.date} 
                            </h3>

                            <c:if test="${visitor.post!=null}">
                                <h3>
                                    ${visitor.post}
                                </h3>
                            </c:if>	

                            <h6>
                                AGENT: ${visitor.userAgent}  
                                <c:if test="${visitor.userAgent==null}">null</c:if>
                            </h6>
                        </div>
                    </c:forEach>  

                </div>
            </div>
        </div>
    </body>
</html>