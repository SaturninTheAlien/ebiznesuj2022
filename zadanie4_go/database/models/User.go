package models

import (
	"database/sql"

	"gorm.io/gorm"
)

type User struct {
	gorm.Model
	Username  string
	Password  string
	FirstName sql.NullString
	LastName  sql.NullString
}
