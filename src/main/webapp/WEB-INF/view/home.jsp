<html>
<head>
    <title>Home</title>
</head>
<body>

<form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/summary">
    <input type="file" name="document">
    <button type="submit">GO</button>
</form>

<div id="result">
</div>

</body>
</html>
