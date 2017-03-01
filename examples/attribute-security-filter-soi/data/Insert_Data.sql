/*
Copyright 2017 Esri

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.​
*/

DECLARE @Agency_Queue TABLE (
	Queue_Name NVARCHAR(255),
	Agency_Name NVARCHAR(50)
);

-- Insert agencies and queues
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT '311 Customer Service Center',  Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE '311%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT '311 Customer Service Center', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Knowledge Solutions%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Adult Probation', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Adult Probation%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Aging and Adult Services', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Aging and Adult Services%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Airport, San Francisco International', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Airport, San Francisco International%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Animal Care and Control', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Animal Care and Control%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Art Commission', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Art Commission%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Asian Art Museum', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Asian Art Museum%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Assessor', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Assessor%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Board of Appeals (BOA)', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Board of Appeals (BOA)%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Board of Supervisors', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Board of Supervisors%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'DPH', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'DPH%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'DPT', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'DPT%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'DPW', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'DPW%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'DPW', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Graffiti Abatement%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Mayors Office', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Mayors Office%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'MUNI', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'MUNI%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Recology', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Recology%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'PUC', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'PUC%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Housing Authority', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Housing Authority%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'PG and E', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'PG and E%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Clear Channel', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Clear Channel%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Treasurer/Tax Collector', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Treasurer/Tax Collector%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'SFMTA', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'SFMTA%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'SFMTA', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'TAXI%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'SSP', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'SSP%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'RPD', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'RPD%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'DBI', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'DBI%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'City Attorney', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'City Attorney%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Police', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Police%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Police', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Noise Report%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'US Postal Service', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'US Postal Service%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'County Clerk', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'County Clerk%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Technology', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Technology%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'SFFD', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'SFFD%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'SFFD', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Fire Department%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Port Authority', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Port Authority%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Planning', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Planning%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Recreation and Parks', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'RecPark%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Recreation and Parks', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Rec and Park%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Entertainment Commission', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Entertainment Commission%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'District Attorney', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'District Attorney%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Rent Board', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Rent Board%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'Environment', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'Environment%';
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT 'HSA', Responsible_Agency FROM Incident WHERE Responsible_Agency LIKE'HSA%';

-- Assign all remaining queues to 311 Customer Service Department
INSERT INTO @Agency_Queue (Agency_Name, Queue_Name) SELECT DISTINCT '311 Customer Service Center', Responsible_Agency FROM Incident i WHERE Responsible_Agency NOT IN (SELECT DISTINCT Queue_Name FROM @Agency_Queue);

UPDATE i SET i.Responsible_Agency = a.Agency_Name FROM Incident i INNER JOIN @Agency_Queue a ON (a.Queue_Name = i.Responsible_Agency);

-- Create some groups and users
DELETE FROM Filter_Group;
INSERT INTO Filter_Group (Group_ID, Group_Name) VALUES (1, 'City Departments');
INSERT INTO Filter_Group (Group_ID, Group_Name) VALUES (2, 'Public');
INSERT INTO Filter_Group (Group_ID, Group_Name) VALUES (3, 'Recology');
INSERT INTO Filter_Group (Group_ID, Group_Name) VALUES (4, 'Clear Channel');
INSERT INTO Filter_Group (Group_ID, Group_Name) VALUES (5, 'US Postal Service');
INSERT INTO Filter_Group (Group_ID, Group_Name) VALUES (6, 'PG and E');

DELETE FROM Agency;
INSERT INTO Agency (Agency_ID, Agency_Name) SELECT ROW_NUMBER() OVER (ORDER BY Responsible_Agency), Responsible_Agency FROM Incident GROUP BY Responsible_Agency ORDER BY Responsible_Agency;

UPDATE i SET i.Responsible_Agency_ID = a.Agency_ID FROM Incident i INNER JOIN Agency a ON (i.Responsible_Agency = a.Agency_Name);

DELETE FROM Filter_Group_Agency;
INSERT INTO Filter_Group_Agency(Group_ID, Agency_ID) SELECT (SELECT Group_ID FROM Filter_Group WHERE Group_Name = 'City Departments'), Agency_ID FROM Agency;
INSERT INTO Filter_Group_Agency(Group_ID, Agency_ID) SELECT (SELECT Group_ID FROM Filter_Group WHERE Group_Name = 'Public'), Agency_ID FROM Agency WHERE Agency_Name IN ('Animal Care and Control', 'DPT', 'DPW', 'MUNI', 'Recology', 'SFMTA', 'Police', 'SFFD', 'Recreation and Parks');
INSERT INTO Filter_Group_Agency(Group_ID, Agency_ID) SELECT (SELECT Group_ID FROM Filter_Group WHERE Group_Name = 'Recology'), Agency_ID FROM Agency WHERE Agency_Name = 'Recology';
INSERT INTO Filter_Group_Agency(Group_ID, Agency_ID) SELECT (SELECT Group_ID FROM Filter_Group WHERE Group_Name = 'Clear Channel'), Agency_ID FROM Agency WHERE Agency_Name = 'Clear Channel';
INSERT INTO Filter_Group_Agency(Group_ID, Agency_ID) SELECT (SELECT Group_ID FROM Filter_Group WHERE Group_Name = 'US Postal Service'), Agency_ID FROM Agency WHERE Agency_Name = 'US Postal Service';
INSERT INTO Filter_Group_Agency(Group_ID, Agency_ID) SELECT (SELECT Group_ID FROM Filter_Group WHERE Group_Name = 'PG and E'), Agency_ID FROM Agency WHERE Agency_Name = 'PG and E';

DELETE FROM Filter_User;
INSERT INTO Filter_User (User_ID, User_Name) VALUES (1, 'call_center_user');
INSERT INTO Filter_User (User_ID, User_Name) VALUES (2, 'public_user');
INSERT INTO Filter_User (User_ID, User_Name) VALUES (3, 'recology_user');
INSERT INTO Filter_User (User_ID, User_Name) VALUES (4, 'clear_channel_user');
INSERT INTO Filter_User (User_ID, User_Name) VALUES (5, 'usps_user');
INSERT INTO Filter_User (User_ID, User_Name) VALUES (6, 'pge_user');

INSERT INTO Filter_User_Group (User_ID, Group_ID) SELECT (SELECT User_ID from Filter_User WHERE User_Name = 'call_center_user'), Group_ID FROM Filter_Group WHERE Group_Name = 'City Departments';
INSERT INTO Filter_User_Group (User_ID, Group_ID) SELECT (SELECT User_ID from Filter_User WHERE User_Name = 'public_user'), Group_ID FROM Filter_Group WHERE Group_Name = 'Public';
INSERT INTO Filter_User_Group (User_ID, Group_ID) SELECT (SELECT User_ID from Filter_User WHERE User_Name = 'recology_user'), Group_ID FROM Filter_Group WHERE Group_Name = 'Recology';
INSERT INTO Filter_User_Group (User_ID, Group_ID) SELECT (SELECT User_ID from Filter_User WHERE User_Name = 'clear_channel_user'), Group_ID FROM Filter_Group WHERE Group_Name = 'Clear Channel';
INSERT INTO Filter_User_Group (User_ID, Group_ID) SELECT (SELECT User_ID from Filter_User WHERE User_Name = 'usps_user'), Group_ID FROM Filter_Group WHERE Group_Name = 'US Postal Service';
INSERT INTO Filter_User_Group (User_ID, Group_ID) SELECT (SELECT User_ID from Filter_User WHERE User_Name = 'pge_user'), Group_ID FROM Filter_Group WHERE Group_Name = 'PG and E';

