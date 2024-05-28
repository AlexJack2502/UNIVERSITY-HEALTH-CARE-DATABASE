--showAuthenticateQuery
DECLARE @UserName AS VARCHAR(20) = ?
DECLARE @Pwd AS VARCHAR(255) = ?
SELECT
CASE
WHEN U.U_ID = S.User_ID AND S.User_ID IS NOT NULL THEN 'Student'
WHEN U.U_ID = D.User_ID AND D.User_ID IS NOT NULL THEN 'Doctor'
ELSE NULL
END AS Role,
CASE
WHEN U.U_ID = S.User_ID AND S.User_ID IS NOT NULL THEN S.St_ID
WHEN U.U_ID = D.User_ID AND D.User_ID IS NOT NULL THEN D.D_ID
ELSE NULL
END AS ID
FROM [Account].[User] U
FULL JOIN [Account].[Student] S ON U.U_ID = S.User_ID
FULL JOIN [Account].[Doctor] D ON U.U_ID = D.User_ID
WHERE U.User_name = @UserName AND U.Password = @Pwd

--submitPasswordUpdate
DECLARE @UserName AS VARCHAR(20) = ?
DECLARE @oldPwd AS VARCHAR(255) = ?
DECLARE @newPwd AS VARCHAR(255) = ?
UPDATE [Account].[User]
SET Password = @newPwd
WHERE User_name = @UserName AND Password = @oldPwd

--submitHealingUpdate
DECLARE @AID INT
SELECT @AID = MAX(A_ID) + 1 FROM Appointment.Appointment
INSERT INTO [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus])
VALUES (@AID, ?, ?, ?)
INSERT INTO [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID])
VALUES (@AID, 1)
INSERT INTO [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID])
VALUES (@AID, NULL)
DECLARE @BID INT
SELECT @BID = MAX(BID) + 1 FROM Billing.Billing
INSERT INTO [Billing].Billing(BID, Price, Appointment_ID, Insurerace)
VALUES (@BID, 0.00, @AID, 0)

--showDoctorBookingQuery
SELECT A.A_ID AS [ID], A.Date AS [Date], B.Price AS [Price], CONCAT(S.LastName , ' ' , S.FirstName) AS [FullName], S.Gender AS [Gender], S.PhoneNumber AS [Phone], 'Booked' AS State
FROM Appointment.Student_Appointment SA
JOIN Appointment.Appointment A ON SA.Appointment_ID = A.A_ID
JOIN Billing.Billing B ON SA.Appointment_ID = B.Appointment_ID
JOIN Account.Student S ON SA.Student_ID = S.St_ID
JOIN Appointment.Doctor_Appointment DA ON SA.Appointment_ID = DA.Appointment_ID
WHERE DA.Doctor_ID = ?
UNION ALL
SELECT DA.Appointment_ID, A.Date, B.Price, 'NULL' AS FullName, 'NULL' AS Gender, 'NULL' AS PhoneNumber, NULL AS State
FROM Appointment.Doctor_Appointment DA
JOIN Appointment.Appointment A ON DA.Appointment_ID = A.A_ID
JOIN Billing.Billing B ON DA.Appointment_ID = B.Appointment_ID
LEFT JOIN Appointment.Student_Appointment SA ON DA.Appointment_ID = SA.Appointment_ID
WHERE SA.Student_ID IS NULL AND DA.Doctor_ID = ?

--delistHealingUpdate
SELECT A.Date
FROM Appointment.Appointment A
INNER JOIN Appointment.Doctor_Appointment DA ON DA.Appointment_ID = A.A_ID
WHERE A.Date >= CAST(GETDATE() AS DATE)
DELETE FROM [Appointment].[Doctor_Appointment]
WHERE Doctor_ID = ? AND Appointment_ID = ?

--checkID
SELECT * FROM [Appointment].[Doctor_Appointment]
WHERE Doctor_ID = ? AND Appointment_ID = ?

--showNameQuery
DECLARE @UserID AS INT = ?
DECLARE @Role AS VARCHAR(30) = ?
SELECT CONCAT(LastName, ' ', FirstName) AS [FullName]
FROM (
SELECT
CASE
WHEN @UserID = S.St_ID AND @Role = 'Student' THEN S.FirstName
WHEN @UserID = D.D_ID AND @Role = 'Doctor' THEN D.FirstName
END AS FirstName,
CASE
WHEN @UserID = S.St_ID AND @Role = 'Student' THEN S.LastName
WHEN @UserID = D.D_ID AND @Role = 'Doctor' THEN D.LastName
END AS LastName
FROM [Account].[User] U
FULL JOIN [Account].[Student] S ON U.U_ID = S.User_ID
FULL JOIN [Account].[Doctor] D ON U.U_ID = D.User_ID) AS SUBQUERY
WHERE FirstName IS NOT NULL AND LastName IS NOT NULL

--healingUpdate
DECLARE @AppointmentID AS INT = ?
DECLARE @date AS DATE = ?
DECLARE @price AS INT = ?
DECLARE @healthStatus AS VARCHAR(50) = ?
DECLARE @bookingStatus AS VARCHAR(20) = ?
UPDATE Appointment.Appointment
SET Date = @date, HealthStatus = @healthStatus, BookingStatus = @bookingStatus
WHERE A_ID = @AppointmentID
UPDATE Billing.Billing
SET Price = @price
WHERE Appointment_ID = @AppointmentID

--showSearchQuery
SELECT T.IllnessType AS [Symptom], M.Name AS [Solution], M.Description AS [Description], M.Name AS [Medicine], M.Quantity AS [Quantity]
FROM [Treatment].[Treatment] T, [Treatment].[Medicine] M
WHERE T.Treat_ID = M.Treatment_ID AND T.IllnessType = ?

--cancelHealingUpdate
SELECT A.Date
FROM Appointment.Appointment A
INNER JOIN Appointment.Student_Appointment SA ON SA.Appointment_ID = A.A_ID
WHERE A.Date >= CAST(GETDATE() AS DATE)
DELETE FROM [Appointment].[Student_Appointment]
WHERE Student_ID = ? AND Appointment_ID = ?

--showStudentBookingQuery
SELECT A.A_ID AS [ID], A.Date AS [Date], D.FirstName AS [FirstName], D.LastName AS [LastName], D.PhoneNumber AS [Phone]
FROM [Appointment].[Appointment] A
INNER JOIN [Appointment].[Doctor_Appointment] DA ON DA.Appointment_ID = A.A_ID
INNER JOIN [Account].[Doctor] D ON D.D_ID = DA.Doctor_ID
INNER JOIN [Appointment].[Student_Appointment] SA ON SA.Appointment_ID = A.A_ID
INNER JOIN [Account].[Student] S ON S.St_ID = SA.Student_ID
WHERE S.St_ID = ? AND A.Date >= CAST(GETDATE() AS DATE)
ORDER BY A.Date ASC

--showAvailableAppointmentQuery
SELECT A.A_ID, A.Date, B.Price, D.FirstName, D.LastName, D.Gender, SA.Student_ID
FROM [Appointment].[Appointment] A
INNER JOIN [Billing].[Billing] B ON A.A_ID = B.Appointment_ID
INNER JOIN [Appointment].[Doctor_Appointment] DA ON A.A_ID = DA.Appointment_ID
INNER JOIN [Account].[Doctor] D ON D.D_ID = DA.Doctor_ID
INNER JOIN [Appointment].[Student_Appointment] SA ON SA.Appointment_ID = A.A_ID
WHERE  SA.Student_ID is null and Date >= CAST(GETDATE() AS DATE) ORDER BY A.Date ASC

--submitStudentHealingUpdate
DECLARE @AID INT
SELECT @AID = A_ID FROM Appointment.Appointment
WHERE A_ID = 13
UPDATE Appointment.Student_Appointment
SET Student_ID = 21091
WHERE Appointment_ID = @AID
UPDATE Billing.Billing
SET Insurerace = 1
WHERE Appointment_ID = @AID

--submitDoctorUser
--String1
DECLARE @UserName AS VARCHAR(20) = ?
DECLARE @Pwd AS VARCHAR(255) = ?
INSERT INTO [Account].[User] ([User_name], [Password])
VALUES (@UserName, @Pwd)
--String2
INSERT INTO [Account].[Doctor] ([D_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Email], [PhoneNumber])
VALUES (?, (SELECT U_ID FROM [Account].[User] U WHERE U.User_name = ?), ?, ?,?, ?, ?, ?)

--submitStudentUser
--String1
DECLARE @UserName AS VARCHAR(20) = ?
DECLARE @Pwd AS VARCHAR(255) = ?
INSERT INTO [Account].[User] ([User_name], [Password])
VALUES (@UserName, @Pwd)
--String2
INSERT INTO [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major])
VALUES (?, (SELECT U_ID FROM [Account].[User] U WHERE U.User_name = ?), ?, ?,?, ?, ?, ?, ?)