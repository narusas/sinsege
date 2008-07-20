HTTP Protocol



요청(GET/POST)

GET /ep/map/infoMapView.jsp?code=1&sub1=10&sub2=3 HTTP/1.0\r\n
ParamName: Value.
..
..
\r\n



POST /ep/map/infoMapView.jsp HTTP/1.0
Content-Length: 15
ParamName: Value.
..
..
\r\n
?code=1&sub1=10&sub2=3





응답
HTTP 200 OK\r\n 
Content-Length: 100\r\n
\r\n
asasdasdasdasdasdas





세션관리

GET /ep/map/infoMapView.jsp?code=1&sub1=10&sub2=3 HTTP/1.0\r\n
\r\n

HTTP 200 OK\r\n 
Content-Length: 100\r\n
Cookie: sessionid=1020304\r\n;
\r\n
asasdasdasdasdasdas



GET /ep/map/infoLocationView.jsp?code=1 HTTP/1.0\r\n
Cookie: sessionid=1020304\r\n; 
\r\n




쿠키를 주는 페이지와 사용하는 페이지가 다를수 있음. 필요로 하는 쿠키를 주는 페이지를 먼저 방문하고 와야 되는 경우가 있음. 

