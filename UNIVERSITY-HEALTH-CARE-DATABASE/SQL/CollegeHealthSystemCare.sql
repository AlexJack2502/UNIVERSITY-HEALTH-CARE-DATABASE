USE [Sec_Project]
GO
/****** Object:  Schema [Account]    Script Date: 17/05/2024 11:59:20 PM ******/
CREATE SCHEMA [Account]
GO
/****** Object:  Schema [Appointment]    Script Date: 17/05/2024 11:59:20 PM ******/
CREATE SCHEMA [Appointment]
GO
/****** Object:  Schema [Billing]    Script Date: 17/05/2024 11:59:20 PM ******/
CREATE SCHEMA [Billing]
GO
/****** Object:  Schema [Record]    Script Date: 17/05/2024 11:59:20 PM ******/
CREATE SCHEMA [Record]
GO
/****** Object:  Schema [Treatment]    Script Date: 17/05/2024 11:59:20 PM ******/
CREATE SCHEMA [Treatment]
GO
/****** Object:  Table [Account].[Doctor]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Account].[Doctor](
	[D_ID] [int] NOT NULL,
	[User_ID] [varchar](20) NULL,
	[FirstName] [varchar](255) NOT NULL,
	[LastName] [varchar](255) NOT NULL,
	[Gender] [varchar](20) NULL,
	[DateOfBirth] [date] NULL,
	[Address] [varchar](255) NULL,
	[PhoneNumber] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[D_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Account].[Student]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Account].[Student](
	[St_ID] [int] NOT NULL,
	[User_ID] [varchar](20) NULL,
	[FirstName] [varchar](255) NOT NULL,
	[LastName] [varchar](255) NOT NULL,
	[Gender] [varchar](20) NULL,
	[DateOfBirth] [date] NULL,
	[Address] [varchar](255) NULL,
	[PhoneNumber] [varchar](20) NOT NULL,
	[Major] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[St_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Account].[User]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Account].[User](
	[U_ID] [varchar](20) NOT NULL,
	[User_name] [char](20) NOT NULL,
	[Password] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[U_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Appointment].[Appointment]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Appointment].[Appointment](
	[A_ID] [int] NOT NULL,
	[Date] [date] NULL,
	[HealthStatus] [varchar](50) NULL,
	[BookingStatus] [varchar](20) NULL,
	[Doctor_ID] [int] NULL,
	[Student_ID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[A_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Appointment].[Doctor_Appointment]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Appointment].[Doctor_Appointment](
	[Appointment_ID] [int] NOT NULL,
	[Doctor_ID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Appointment_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Appointment].[Student_Appointment]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Appointment].[Student_Appointment](
	[Appointment_ID] [int] NOT NULL,
	[Student_ID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Appointment_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Billing].[Billing]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Billing].[Billing](
	[BID] [int] NOT NULL,
	[Appointment_ID] [int] NULL,
	[Insurerace] [bit] NOT NULL,
	[Price] [money] NULL,
PRIMARY KEY CLUSTERED 
(
	[BID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Record].[Appointment_Record]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Record].[Appointment_Record](
	[Appointment_ID] [int] NOT NULL,
	[Record_ID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Appointment_ID] ASC,
	[Record_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Record].[Record]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Record].[Record](
	[Record_ID] [int] NOT NULL,
	[Appointment_ID] [int] NULL,
	[Student_ID] [int] NULL,
	[MedicalHistory] [varchar](255) NOT NULL,
	[Date] [date] NULL,
PRIMARY KEY CLUSTERED 
(
	[Record_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Record].[Student_Record]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Record].[Student_Record](
	[Record_ID] [int] NOT NULL,
	[Student_ID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Record_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Treatment].[Medicine]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Treatment].[Medicine](
	[Medicine_ID] [int] NOT NULL,
	[Treatment_ID] [int] NULL,
	[Name] [varchar](255) NOT NULL,
	[Effect] [varchar](50) NOT NULL,
	[Description] [varchar](255) NOT NULL,
	[Quantity] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Medicine_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [Treatment].[Treatment]    Script Date: 17/05/2024 11:59:20 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [Treatment].[Treatment](
	[Treat_ID] [int] NOT NULL,
	[Description] [varchar](255) NOT NULL,
	[IllnessType] [varchar](255) NOT NULL,
	[Appointment_ID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Treat_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [Account].[Doctor] ([D_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber]) VALUES (1, N'D01', N'Thanh Nhan', N'Vo', N'M', CAST(N'1950-05-15' AS Date), N'123 Le Duc Tho St, District 1, HCM City', 338543123)
GO
INSERT [Account].[Doctor] ([D_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber]) VALUES (2, N'D02', N'Thanh Liem', N'Nguyen', N'M', CAST(N'1954-05-15' AS Date), N'254 Le Van Viet St, District 3, HCM City', 337443234)
GO
INSERT [Account].[Doctor] ([D_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber]) VALUES (3, N'D03', N'Tan Sinh', N'Nguyen Thi', N'FM', CAST(N'1951-05-15' AS Date), N'986 Phan Xich Long St, District 10, HCM City', 334113567)
GO
INSERT [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major]) VALUES (20088, N'S04', N'Trong Tin', N'Phan', N'Male', CAST(N'2002-05-15' AS Date), N'678 Doan Thi Diem St, District 6, HCM City', N'0333043678', N'Data Science')
GO
INSERT [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major]) VALUES (21091, N'S01', N'Thien Ngan', N'Le', N'Female', CAST(N'2003-05-15' AS Date), N'123 Le Duc Tho St, District 1, HCM City', N'0338543123', N'Computer Science')
GO
INSERT [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major]) VALUES (21314, N'S02', N'Phuong Thao', N'Nguyen Thi', N'Female', CAST(N'2003-05-15' AS Date), N'234 Phan Xich Long St, District 2, HCM City', N'0337443234', N'Information Technology')
GO
INSERT [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major]) VALUES (22140, N'S03', N'Du Nhan', N'Nguyen', N'Male', CAST(N'2004-05-15' AS Date), N'567 Huynh Tan Phat St, District 5, HCM City', N'0334143567', N'Data Science')
GO
INSERT [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major]) VALUES (22171, N'S05', N'Nguyen Khanh', N'Tran', N'Male', CAST(N'2004-05-15' AS Date), N'789 Nguyen Dinh Chieu St, District 7, HCM City', N'0332943789', N'Data Science')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'D01', N'ThanhNhan           ', N'19500515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'D02', N'ThanhLiem           ', N'19540515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'D03', N'TanSinh             ', N'19510515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'S01', N'ThienNgan           ', N'20030515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'S02', N'PhuongThao          ', N'20030515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'S03', N'DuNhan              ', N'20040515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'S04', N'TrongTin            ', N'20020515')
GO
INSERT [Account].[User] ([U_ID], [User_name], [Password]) VALUES (N'S05', N'NguyenKhanh         ', N'20040515')
GO
INSERT [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus], [Doctor_ID], [Student_ID]) VALUES (1, CAST(N'2024-04-14' AS Date), N'Healthy', N'Confirmed', 1, 21091)
GO
INSERT [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus], [Doctor_ID], [Student_ID]) VALUES (2, CAST(N'2024-04-21' AS Date), N'Checkup', N'Pending', 2, 21314)
GO
INSERT [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus], [Doctor_ID], [Student_ID]) VALUES (3, CAST(N'2024-05-15' AS Date), N'Sick', N'Cancelled', 2, 22140)
GO
INSERT [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus], [Doctor_ID], [Student_ID]) VALUES (4, CAST(N'2024-05-18' AS Date), N'Sick', N'Confirmed', 3, 20088)
GO
INSERT [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus], [Doctor_ID], [Student_ID]) VALUES (5, CAST(N'2024-06-15' AS Date), N'Sick', N'Confirmed', 3, 20088)
GO
INSERT [Appointment].[Appointment] ([A_ID], [Date], [HealthStatus], [BookingStatus], [Doctor_ID], [Student_ID]) VALUES (6, CAST(N'2024-06-16' AS Date), N'Checkup', N'Cancelled', 1, 22171)
GO
INSERT [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID]) VALUES (1, 1)
GO
INSERT [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID]) VALUES (2, 2)
GO
INSERT [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID]) VALUES (3, 2)
GO
INSERT [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID]) VALUES (4, 3)
GO
INSERT [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID]) VALUES (5, 3)
GO
INSERT [Appointment].[Doctor_Appointment] ([Appointment_ID], [Doctor_ID]) VALUES (6, 1)
GO
INSERT [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID]) VALUES (1, 21091)
GO
INSERT [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID]) VALUES (2, 21314)
GO
INSERT [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID]) VALUES (3, 22140)
GO
INSERT [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID]) VALUES (4, 20088)
GO
INSERT [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID]) VALUES (5, 20088)
GO
INSERT [Appointment].[Student_Appointment] ([Appointment_ID], [Student_ID]) VALUES (6, 22171)
GO
INSERT [Billing].[Billing] ([BID], [Appointment_ID], [Insurerace], [Price]) VALUES (24001, 1, 1, 0.0000)
GO
INSERT [Billing].[Billing] ([BID], [Appointment_ID], [Insurerace], [Price]) VALUES (24002, 2, 0, 50.0000)
GO
INSERT [Billing].[Billing] ([BID], [Appointment_ID], [Insurerace], [Price]) VALUES (24003, 3, 0, 60.0000)
GO
INSERT [Billing].[Billing] ([BID], [Appointment_ID], [Insurerace], [Price]) VALUES (24004, 4, 1, 100.0000)
GO
INSERT [Billing].[Billing] ([BID], [Appointment_ID], [Insurerace], [Price]) VALUES (24005, 5, 0, 30.0000)
GO
INSERT [Record].[Appointment_Record] ([Appointment_ID], [Record_ID]) VALUES (1, 122)
GO
INSERT [Record].[Appointment_Record] ([Appointment_ID], [Record_ID]) VALUES (2, 123)
GO
INSERT [Record].[Appointment_Record] ([Appointment_ID], [Record_ID]) VALUES (3, 124)
GO
INSERT [Record].[Appointment_Record] ([Appointment_ID], [Record_ID]) VALUES (4, 125)
GO
INSERT [Record].[Appointment_Record] ([Appointment_ID], [Record_ID]) VALUES (5, 126)
GO
INSERT [Record].[Appointment_Record] ([Appointment_ID], [Record_ID]) VALUES (6, 127)
GO
INSERT [Record].[Record] ([Record_ID], [Appointment_ID], [Student_ID], [MedicalHistory], [Date]) VALUES (122, 1, 21091, N'No previous conditions', CAST(N'2024-04-14' AS Date))
GO
INSERT [Record].[Record] ([Record_ID], [Appointment_ID], [Student_ID], [MedicalHistory], [Date]) VALUES (123, 2, 21314, N'Headache', CAST(N'2024-04-21' AS Date))
GO
INSERT [Record].[Record] ([Record_ID], [Appointment_ID], [Student_ID], [MedicalHistory], [Date]) VALUES (124, 3, 22140, N'Fever', CAST(N'2024-05-15' AS Date))
GO
INSERT [Record].[Record] ([Record_ID], [Appointment_ID], [Student_ID], [MedicalHistory], [Date]) VALUES (125, 4, 20088, N'Food Poisoning', CAST(N'2024-05-18' AS Date))
GO
INSERT [Record].[Record] ([Record_ID], [Appointment_ID], [Student_ID], [MedicalHistory], [Date]) VALUES (126, 5, 20088, N'Fever', CAST(N'2024-06-15' AS Date))
GO
INSERT [Record].[Record] ([Record_ID], [Appointment_ID], [Student_ID], [MedicalHistory], [Date]) VALUES (127, 6, 22171, N'Digestive Disorders', CAST(N'2024-06-16' AS Date))
GO
INSERT [Record].[Student_Record] ([Record_ID], [Student_ID]) VALUES (122, 21091)
GO
INSERT [Record].[Student_Record] ([Record_ID], [Student_ID]) VALUES (123, 21314)
GO
INSERT [Record].[Student_Record] ([Record_ID], [Student_ID]) VALUES (124, 22140)
GO
INSERT [Record].[Student_Record] ([Record_ID], [Student_ID]) VALUES (125, 20088)
GO
INSERT [Record].[Student_Record] ([Record_ID], [Student_ID]) VALUES (126, 20088)
GO
INSERT [Record].[Student_Record] ([Record_ID], [Student_ID]) VALUES (127, 22171)
GO
INSERT [Treatment].[Medicine] ([Medicine_ID], [Treatment_ID], [Name], [Effect], [Description], [Quantity]) VALUES (2120, 459, N'Antacids', N'Pain relief', N'Used for stomach problems and Digestive Disorders', 6)
GO
INSERT [Treatment].[Medicine] ([Medicine_ID], [Treatment_ID], [Name], [Effect], [Description], [Quantity]) VALUES (2248, 402, N'Imodium', N'Pain relief', N'Used for food poisoning', 3)
GO
INSERT [Treatment].[Medicine] ([Medicine_ID], [Treatment_ID], [Name], [Effect], [Description], [Quantity]) VALUES (2302, 426, N'Panadol', N'Pain relief', N'Used for headache', 12)
GO
INSERT [Treatment].[Medicine] ([Medicine_ID], [Treatment_ID], [Name], [Effect], [Description], [Quantity]) VALUES (2424, 437, N'Paracetamol', N'Pain relief', N'Used for pain and fever', 12)
GO
INSERT [Treatment].[Treatment] ([Treat_ID], [Description], [IllnessType], [Appointment_ID]) VALUES (402, N'Used for food poisoning', N'E.coli', 4)
GO
INSERT [Treatment].[Treatment] ([Treat_ID], [Description], [IllnessType], [Appointment_ID]) VALUES (426, N'Used for headache', N'Tension Headache', 2)
GO
INSERT [Treatment].[Treatment] ([Treat_ID], [Description], [IllnessType], [Appointment_ID]) VALUES (437, N'Used for pain and fever', N'Environmental fever', 3)
GO
INSERT [Treatment].[Treatment] ([Treat_ID], [Description], [IllnessType], [Appointment_ID]) VALUES (459, N'Used for stomach problems and Digestive Disorders', N'Lactose Intolerance', 5)
GO
ALTER TABLE [Account].[Doctor]  WITH CHECK ADD FOREIGN KEY([User_ID])
REFERENCES [Account].[User] ([U_ID])
GO
ALTER TABLE [Account].[Student]  WITH CHECK ADD FOREIGN KEY([User_ID])
REFERENCES [Account].[User] ([U_ID])
GO
ALTER TABLE [Appointment].[Appointment]  WITH CHECK ADD FOREIGN KEY([Doctor_ID])
REFERENCES [Account].[Doctor] ([D_ID])
GO
ALTER TABLE [Appointment].[Appointment]  WITH CHECK ADD FOREIGN KEY([Student_ID])
REFERENCES [Account].[Student] ([St_ID])
GO
ALTER TABLE [Appointment].[Doctor_Appointment]  WITH CHECK ADD FOREIGN KEY([Appointment_ID])
REFERENCES [Appointment].[Appointment] ([A_ID])
GO
ALTER TABLE [Appointment].[Doctor_Appointment]  WITH CHECK ADD FOREIGN KEY([Doctor_ID])
REFERENCES [Account].[Doctor] ([D_ID])
GO
ALTER TABLE [Appointment].[Student_Appointment]  WITH CHECK ADD FOREIGN KEY([Appointment_ID])
REFERENCES [Appointment].[Appointment] ([A_ID])
GO
ALTER TABLE [Appointment].[Student_Appointment]  WITH CHECK ADD FOREIGN KEY([Student_ID])
REFERENCES [Account].[Student] ([St_ID])
GO
ALTER TABLE [Billing].[Billing]  WITH CHECK ADD FOREIGN KEY([Appointment_ID])
REFERENCES [Appointment].[Appointment] ([A_ID])
GO
ALTER TABLE [Record].[Appointment_Record]  WITH CHECK ADD FOREIGN KEY([Appointment_ID])
REFERENCES [Appointment].[Appointment] ([A_ID])
GO
ALTER TABLE [Record].[Appointment_Record]  WITH CHECK ADD FOREIGN KEY([Record_ID])
REFERENCES [Record].[Record] ([Record_ID])
GO
ALTER TABLE [Record].[Record]  WITH CHECK ADD FOREIGN KEY([Appointment_ID])
REFERENCES [Appointment].[Appointment] ([A_ID])
GO
ALTER TABLE [Record].[Record]  WITH CHECK ADD FOREIGN KEY([Student_ID])
REFERENCES [Account].[Student] ([St_ID])
GO
ALTER TABLE [Record].[Student_Record]  WITH CHECK ADD FOREIGN KEY([Record_ID])
REFERENCES [Record].[Record] ([Record_ID])
GO
ALTER TABLE [Record].[Student_Record]  WITH CHECK ADD FOREIGN KEY([Student_ID])
REFERENCES [Account].[Student] ([St_ID])
GO
ALTER TABLE [Treatment].[Medicine]  WITH CHECK ADD FOREIGN KEY([Treatment_ID])
REFERENCES [Treatment].[Treatment] ([Treat_ID])
GO
ALTER TABLE [Treatment].[Treatment]  WITH CHECK ADD FOREIGN KEY([Appointment_ID])
REFERENCES [Appointment].[Appointment] ([A_ID])
GO
