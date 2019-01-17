<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
<head>
    <title>Parts Page</title>

    <style type="text/css">
        .tg {
            border-collapse: collapse;
            border-spacing: 0;
            border-color: #ccc;
        }

        .tg td {
            font-family: Arial, sans-serif;
            font-size: 14px;
            padding: 10px 5px;
            border-style: solid;
            border-width: 1px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            color: #333;
            background-color: #fff;
        }

        .tg th {
            font-family: Arial, sans-serif;
            font-size: 14px;
            font-weight: normal;
            padding: 10px 5px;
            border-style: solid;
            border-width: 1px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            color: #333;
            background-color: #f0f0f0;
        }

        .tg .tg-4eph {
            background-color: #f9f9f9
        }
    </style>
</head>
<body>

<h1>Part List</h1>

<c:url var="filterParts" value="/parts/filter"/>

<c:if test="${!empty listParts}">
    <form:form action="${filterParts}">
        <td width="100" align="center">
            <label><input name="filter" type="radio" checked="checked"
                          value="${filter="all"}"/>All</label>
        </td>
        <td width="100" align="center">
            <label><input name="filter" type="radio"
                          value="${filter="required"}"/>Mandatory</label>
        </td>
        <td width="100" align="center">
            <label><input name="filter" type="radio"
                          value="${filter="optional"}"/>Optional</label>
        </td>
        <td width="100" align="center">
            <input type="submit" class="input"
                   value="<spring:message text="Choose"/>"/>
        </td>
    </form:form>

    <table class="tg">
        <tr>
            <th width="80">ID</th>
            <th width="120">Title</th>
            <th width="120">Requared</th>
            <th width="120">Quantity</th>
            <th width="60">Edit</th>
            <th width="60">Delete</th>
        </tr>
        <c:forEach items="${listParts}" var="part">
            <tr>
                <td>${part.id}</td>
                <td><a href="/partdata/${part.id}" target="_blank">${part.name}</a></td>
                <td>${part.necessary}</td>
                <td>${part.quantity}</td>
                <td><a href="<c:url value='/edit/${part.id}'/>">Edit</a></td>
                <td><a href="<c:url value='/remove/${part.id}'/>">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty listParts}">
    <p class="ii">The listParts is empty!((((( But You may want someone to add right now! Do it!</p>
</c:if>

<br/>


<c:if test="${partFound==null}">
    <table class="tg">
        <tr>
            <td width="120">May be assablied</td>
            <td width="120" align="center">${ready} computer(s)</td>
        </tr>
    </table>
</c:if>


<table>
    <tr>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td width="50%">

            <h1>Add a Part</h1>

            <c:url var="addAction" value="/parts/add"/>

            <form:form action="${addAction}" modelAttribute="part">
                <table>
                    <c:if test="${!empty part.name}">
                        <tr>
                            <td>
                                <form:label path="id">
                                    <spring:message text="ID"/>
                                </form:label>
                            </td>
                            <td>
                                <form:input path="id" readonly="true" size="8" disabled="true"/>
                                <form:hidden path="id"/>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>
                            <form:label path="name">
                                <spring:message text="Title"/>
                            </form:label>
                        </td>
                        <td>
                            <form:input path="name"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form:label path="necessary">
                                <spring:message text="Requared"/>
                            </form:label>
                        </td>
                        <td>
                            <form:input path="necessary"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form:label path="quantity">
                                <spring:message text="Quantity"/>
                            </form:label>
                        </td>
                        <td>
                            <form:input path="quantity"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <c:if test="${!empty part.name}">
                                <input type="submit"
                                       value="<spring:message text="Edit Part"/>"/>
                            </c:if>
                            <c:if test="${empty part.name}">
                                <input type="submit"
                                       value="<spring:message text="Add Part"/>"/>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>

        <td>
            <h1>Search a Part by Name</h1>

            <c:url value="/search" var="searchAction"/>
            <form:form action="${searchAction}" method="POST">
                <label>
                    <input type="text" name="partname"/>
                </label>
                <input type="submit" value="search"/> </form:form>

        </td>
    </tr>
</table>
<div id="pagination" class="uu">
    <p>Pagination: </p>

    <c:url value="/parts" var="prev">
        <c:param name="page" value="${page - 1}"/>
    </c:url>
    <c:if test="${page > 1}">
        <a href="<c:out value="${prev}" />" class="pn prev">Prev</a>
    </c:if>

    <c:forEach begin="1" end="${maxPages}" step="1" varStatus="i">
        <c:choose>
            <c:when test="${page == i.index}">
                <span>${i.index}</span>
            </c:when>
            <c:otherwise>
                <c:url value="/parts" var="url">
                    <c:param name="page" value="${i.index}"/>
                </c:url>
                <a href='<c:out value="${url}" />'>${i.index}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:url value="/parts" var="next">
        <c:param name="page" value="${page + 1}"/>
    </c:url>
    <c:if test="${page + 1 <= maxPages}">
        <a href='<c:out value="${next}" />' class="pn next">Next</a>
    </c:if>
</div>
</body>
</html>
