package models

import (
	"database/sql"

	"gorm.io/gorm"
)

type Product struct {
	gorm.Model
	Name        string
	Price       uint
	Description sql.NullString
	Picture_url sql.NullString
	CategoryID  int
	Category    ProductCategory
}
