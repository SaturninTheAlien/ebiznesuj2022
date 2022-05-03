package models

import "gorm.io/gorm"

type ShoppingCart struct {
	gorm.Model
	UserID    int
	User      User
	ProductID int
	Product   Product
}
