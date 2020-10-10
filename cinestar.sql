use master
go
create database Cinestar
go
use Cinestar
go

create table Movie(
	IDMovie int primary key identity,
	Title nvarchar(50),
	PubDate nvarchar(50),
	Descript nvarchar(3000),
	OriginalName nvarchar(50),
	DirectorID int,
	Duration int,
	Genre nvarchar(50),
	Poster nvarchar(200),
	Link nvarchar(200),
	InCinemasFrom date
)
go

create table Director(
	IDDirector int primary key identity,
	NameSurname nvarchar(50)
)
go

create table Actor(
	IDActor int primary key identity,
	NameSurname nvarchar(50)
)
go



create table MovieActor(
	IDMovieActor int primary key identity,
	MovieID int,
	ActorID int
)
go

create table Account(
	IDAccount int primary key identity,
	Username nvarchar(50) not null,
	Password nvarchar(50) not null,
	Type int DEFAULT 2 not null
)
go


ALTER TABLE Movie
ADD FOREIGN KEY (DirectorID) REFERENCES Director(IDDirector)
go

ALTER TABLE MovieActor
ADD FOREIGN KEY (MovieID) REFERENCES Movie(IDMovie)
go

ALTER TABLE MovieActor
ADD FOREIGN KEY (ActorID) REFERENCES Actor(IDActor)
go

insert into Account values ('admin','password',1)
go



create PROCEDURE createMovie
	@Title NVARCHAR(300),
	@PubDate NVARCHAR(50),
	@Descript NVARCHAR(3000),
	@OriginalName NVARCHAR(50),
	@DirectorNameSurname NVARCHAR(50),
	@Duration int,
	@Genre NVARCHAR(50),
	@Poster NVARCHAR(200),
	@Link NVARCHAR(200),
	@Id INT OUTPUT
AS 
IF NOT EXISTS(select * from Director where NameSurname=@DirectorNameSurname) 
	BEGIN
		insert into Director values (@DirectorNameSurname)
		INSERT INTO Movie (Title, PubDate, Descript, OriginalName,DirectorID,Duration,Genre,Poster,Link)
		VALUES(@Title, @PubDate, @Descript, @OriginalName,SCOPE_IDENTITY(),@Duration,@Genre,@Poster,@Link)
		SET @Id = SCOPE_IDENTITY()
	END
ELSE 
	BEGIN
		INSERT INTO Movie (Title, PubDate, Descript, OriginalName,DirectorID,Duration,Genre,Poster,Link)
		VALUES(@Title, @PubDate, @Descript, @OriginalName,(select IDDirector from Director where NameSurname=@DirectorNameSurname),@Duration,@Genre,@Poster,@Link)
		SET @Id = SCOPE_IDENTITY()
	END
GO


create PROCEDURE createActor
	@MovieID int,
	@NameSurname NVARCHAR(50),
	@Id INT OUTPUT
AS 
IF NOT EXISTS(select * from Actor where NameSurname=@NameSurname) 
	BEGIN
		INSERT INTO Actor values (@NameSurname)
		INSERT INTO MovieActor VALUES(@MovieID,SCOPE_IDENTITY())
		SET @Id = SCOPE_IDENTITY()
	END
ELSE if NOT EXISTS(select * from MovieActor where MovieID=@MovieID and actorid=(select IDActor from Actor where NameSurname= @NameSurname))
	BEGIN
		INSERT INTO MovieActor VALUES(@MovieID,(select IDActor from Actor where NameSurname=@NameSurname))
		SET @Id = SCOPE_IDENTITY()
	END
GO



CREATE PROCEDURE selectMovies
AS 
BEGIN 
	SELECT m.*,d.NameSurname as DirectorNameSurname
	FROM Movie as m
	left join Director as d
	on m.DirectorID=d.IDDirector
END
GO



create PROCEDURE selectMovie
	@IdMovie INT
AS 
BEGIN 
	SELECT m.*,d.NameSurname as DirectorNameSurname
	FROM Movie as m
	inner join Director as d
	on m.DirectorID=d.IDDirector
	where m.IDMovie=@IdMovie
END
GO


create PROCEDURE selectActors
	@IdMovie INT
AS 
BEGIN 
	select a.NameSurname as ActorNameSurname
	from MovieActor as ma
	inner join Actor as a
	on ma.ActorID=a.IDActor
	where MovieID=@IdMovie
END
GO



CREATE PROCEDURE deleteMovie
	@IDMovie INT	 
AS 
BEGIN
	DELETE FROM MovieActor
	WHERE MovieID=@IDMovie
	DELETE FROM Movie
	WHERE IDMovie=@IDMovie
END
GO


CREATE PROCEDURE updateMovie
	@IDMovie INT,
	@Title NVARCHAR(300),
	@PubDate NVARCHAR(50),
	@Descript NVARCHAR(3000),
	@OriginalName NVARCHAR(50),
	@DirectorNameSurname NVARCHAR(50),
	@Duration int,
	@Genre NVARCHAR(50),
	@Poster NVARCHAR(200),
	@Link NVARCHAR(200)
AS
IF NOT EXISTS(select * from Director where NameSurname=@DirectorNameSurname) 
	BEGIN
		insert into Director values (@DirectorNameSurname)
		UPDATE Movie SET Title=@Title, PubDate=@PubDate, Descript=@Descript, OriginalName=@OriginalName,
		DirectorID=SCOPE_IDENTITY(),Duration=@Duration,Genre=@Genre,Poster=@Poster,Link=@Link
		WHERE IDMovie = @IDMovie
	END
ELSE 
	BEGIN
		UPDATE Movie SET Title=@Title, PubDate=@PubDate, Descript=@Descript, OriginalName=@OriginalName,
		DirectorID=(select IDDirector from Director where NameSurname=@DirectorNameSurname),Duration=@Duration,Genre=@Genre,Poster=@Poster,Link=@Link
		WHERE IDMovie = @IDMovie
	END
GO





CREATE PROCEDURE clearDatabase
AS 
BEGIN 
	delete from Account
	delete from MovieActor
	delete from Actor
	delete from Movie
	delete from Director
	insert into Account values ('admin','password',1)
END
GO


create PROCEDURE selectAllActors
AS 
BEGIN 
	select NameSurname from Actor
END
GO

create PROCEDURE selectDirectors
AS 
BEGIN 
	select * from Director
END
GO



create PROCEDURE updateMovieDirector
	@movieID int,
	@director nvarchar(50)
AS 
BEGIN 
	UPDATE Movie SET DirectorID=(select IDDirector from Director where NameSurname=@director) where IDMovie=@movieID
END
GO

create PROCEDURE updateActor
	@selectedActor nvarchar(50),
	@newActor nvarchar(50)
AS 
BEGIN 
	UPDATE Actor SET NameSurname=@newActor where NameSurname=@selectedActor
END
GO


create PROCEDURE deleteActor
	@selectedActor nvarchar(50)
AS 
BEGIN 
	delete from MovieActor
	where ActorID=(select IDActor from Actor where NameSurname=@selectedActor)
	delete from Actor
	where NameSurname=@selectedActor
END
GO

create PROCEDURE createActorOnly
	@NameSurname nvarchar(50)
AS 
IF NOT EXISTS(select * from Actor where NameSurname=@NameSurname) 
	BEGIN
		INSERT INTO Actor values (@NameSurname)
	END
GO



create PROCEDURE updateDirector
	@selectedDirector nvarchar(50),
	@newDirector nvarchar(50)
AS 
BEGIN 
	UPDATE Director SET NameSurname=@newDirector where NameSurname=@selectedDirector
END
GO


create PROCEDURE createDirector
	@NameSurname nvarchar(50)
AS 
IF NOT EXISTS(select * from Director where NameSurname=@NameSurname) 
	BEGIN
		INSERT INTO Director values (@NameSurname)
	END
GO


create PROCEDURE deleteDirector
	@selectedDirector nvarchar(50),
	@rez INT OUTPUT
AS 
IF NOT EXISTS(select * from Movie where DirectorId=(select IDDirector from Director where NameSurname=@selectedDirector))
	BEGIN 
		delete from Director
		where NameSurname=@selectedDirector
		set @rez=1
	END
else
	BEGIN 
		set @rez=0
	END
GO



create PROCEDURE createAccount
	@Username nvarchar(50),
	@Password nvarchar(50),
	@type int OUTPUT
AS 
IF NOT EXISTS(select * from Account where Username=@Username) 
	BEGIN
		INSERT INTO Account (Username,Password) values (@Username,@Password)
		select @type=Type from Account where Username=@Username and Password=@Password
	END
else
	set @type=-1
GO

create PROCEDURE selectAccount
	@Username nvarchar(50),
	@Password nvarchar(50),
	@type int OUTPUT
AS 
IF EXISTS(select * from Account where Username=@Username and Password=@Password) 
	BEGIN
		select @type=Type from Account where Username=@Username and Password=@Password
	END
else IF EXISTS(select * from Account where Username=@Username) 
	BEGIN
		set @type=-2
	END
else
	BEGIN
		set @type=-1
	END
GO
