CREATE (b1:Widget {code:'Button1'})
CREATE (b2:Widget {code:'Button2'})
CREATE (p1:Widget {code:'Panel1'})
CREATE (w1:Widget {code:'Window1'})
CREATE (button:WidgetType {code:'Button'})
CREATE (window:WidgetType {code:'Window'})
CREATE (panel:WidgetType {code:'Panel'})
CREATE
(b1)-[:HAS_TYPE]->(button),
(b2)-[:HAS_TYPE]->(button),
(p1)-[:HAS_TYPE]->(panel),
(w1)-[:HAS_TYPE]->(window),
(w1)-[:HAS_CHILD]->(p1),
(p1)-[:HAS_CHILD]->(b1)
