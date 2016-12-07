#RU Worth It
<p>is a semester project for a database class <b>(Rutgers NB: CS336:Introduction to Data and Information Management)</b> taken with Tomaz Imelinski.
<br>
<br>Contributors:
<br>> Ing Shaun Siek -- Frontend
<br>> Snigdha Paka -- Backend</p>


<h2>URL: </h2>
http://ec2-54-91-144-188.compute-1.amazonaws.com:8080/RU-Worth-It/index.h2>
<br>As far as we know, it runs on mobile, Chrome, Safari, Firefox and IE.

### Used [Bootstrap](http://startbootstrap.com/) and template [Grayscale](http://startbootstrap.com/template-overviews/grayscale/)
### Used MySQL, AWS, Apache TomCat
### Used HTML, CSS, Javascript, Jquery & AJAX
### Click here for [ER Diagram*](https://github.com/ishaunsiek/RU-Worth-It/blob/backend-merge/ER-Diagram.pdf)
<p>*ER Diagram entity and attribute names are modified a little to be more descriptive</p>

<h2>QUERIES:</h2>
<pre>
<h4>These 2 queries populate our College and Stream tabs in the Data section:</h4>
SELECT 
    stream, rank, job, mid, common_major
FROM
    PayscaleJobs
WHERE
    stream IN [input_stream]
LIMIT 5000;

SELECT 
    I.state,
    P.stream,
    P.rank,
    P.name,
    I.early,
    I.mid,
    I.net_price
FROM
    PayscaleStreams P,
    IpedSchool I
WHERE
    I.id = P.id
    AND
    stream IN [input_stream]
    AND
    state IN [input_states];
<h4>These 3 queries populate the Streams tables in the Miscellaneous section: </h4>
SELECT 
    stream, rank, job, AVG(mid), common_major
FROM
    PayscaleJobs
WHERE
    stream IN [input_stream]
GROUP BY stream
LIMIT 5000;

SELECT 
    stream, rank, job, AVG(mid), common_major
FROM
    PayscaleJobs
WHERE
    stream IN [input_stream]
GROUP BY stream , common_major
LIMIT 5000;

SELECT stream, rank, job, common_major, mid FROM(
    SELECT * , @row_number: = CASE WHEN@ common_major = common_major THEN@ row_number + 1 ELSE 1 END AS row_number, @common_major: = common_major AS common_major2 FROM PayscaleJobs, (SELECT@ row_number: = 0, @common_major: = '') AS t ORDER BY common_major asc, mid desc
) AS P
where P.row_number 	&#60; 6
and stream iIN [input_stream];
<h4>These 3 queries populate the College tables in the Miscellaneous section:</h4>
SELECT 
    I.state,
    P.stream,
    P.rank,
    P.name,
    P.early,
    P.mid,
    I.net_price,
    I.meaning
FROM
    PayscaleStreams P,
    IpedSchool I
WHERE
    I.id = P.id AND state IN [input_state]
        AND stream IN [input_stream]
ORDER BY meaning DESC
LIMIT 10;

SELECT 
    I.state,
    P.stream,
    P.rank,
    P.name,
    AVG(P.early),
    AVG(P.mid),
    AVG(I.net_price),
    I.meaning
FROM
    PayscaleStreams P,
    IpedSchool I
WHERE
    I.id = P.id AND state IN [input_state]
        AND stream IN [input_stream]
GROUP BY state , stream
ORDER BY stream , rank
LIMIT 5000;

SELECT * FROM(
    SELECT * , @row_number: = CASE WHEN@ stream = stream THEN@ row_number + 1 ELSE 1 END AS row_number, @stream: = stream AS stream2 FROM IpedSchool, (SELECT@ row_number: = 0, @stream: = '') AS t ORDER BY stream asc, rank asc
) AS P
where P.row_number 	&#60; 6
and stream in [input_stream];
<h4>Other queries used to get data, but not necessarily displayed.</h4>
SELECT 
    state,
    MIN(early),
    MAX(early),
    MIN(mid),
    MAX(mid),
    MIN(net_price),
    MAX(net_price)
FROM
    IpedSchool
WHERE
    state IN [input_state]
    AND stream IN [input_stream]
GROUP BY state;
</pre>
