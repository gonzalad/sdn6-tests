CREATE (of1:ObjetMaquette {code:'OF1', idDefinition: 'a4f901ab-0bdc-4fb6-a734-19dc44f99303'})
CREATE (of2:ObjetMaquette {code:'OF2', idDefinition: 'f57a0d06-b333-45ac-9239-442fb6f00cb3'})
CREATE (f1:ObjetMaquette:Formation {code:'F1', idDefinition: '7344b15d-0268-47b7-ab00-a81fea73859d'})
CREATE (tof:NoeudType {code:'PT'})
CREATE (tf:NoeudType {code:'Formation'})
CREATE
(of1)-[:EST_DE_TYPE]->(tof),
(of2)-[:EST_DE_TYPE]->(tof),
(f1)-[:A_POUR_ENFANT]->(of1)
