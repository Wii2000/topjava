<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <style>
        label {
            display: inline-block;
            width: 140px;
            text-align: left;
        }
        .button {
            font-size: 50px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>${empty meal.id ? 'Adding new meal' : 'Meals Edit'}</h3>
<form method="POST" action='meals'>
    <input hidden="true" type="text" name="id" value="${meal.id}">

    <label for="dateTime">DateTime:</label>
    <input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}"><br><br>

    <label for="description">Description:</label>
    <input type="text" id="description" name="description" value="${meal.description}"><br><br>

    <label for="calories">Calories:</label>
    <input type="text" id="calories" name="calories" value="${meal.calories}"><br><br>

    <input class="button" type="submit" value="${empty meal.id ? 'Add' : 'Save'}">
    <a href="meals">Cancel</a>
</form>
</body>
</html>
