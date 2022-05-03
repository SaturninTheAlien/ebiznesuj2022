package database

import (
	"example/hello/database/models"

	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func Connect() *gorm.DB {
	db, err := gorm.Open(sqlite.Open("Sklep.db"))
	if err != nil {
		panic("Błąd bazy danych")
	}

	db.AutoMigrate(&models.User{})
	db.AutoMigrate(&models.ProductCategory{})
	db.AutoMigrate(&models.Product{})
	db.AutoMigrate(&models.ShoppingCart{})

	return db
}
