package routes

import (
	"example/hello/database/models"
	"strconv"

	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
)

func AddShoppingCartRoutes(e *echo.Echo, db *gorm.DB) {
	g := e.Group("/cart")
	g.GET("/user/:user_id", func(c echo.Context) error {
		var cart []models.ShoppingCart
		db.Where("UserID = ?", c.Param("user_id")).Find(&cart)
		return c.JSON(200, cart)
	})

	g.POST("/user/:user_id", func(c echo.Context) error {
		tmp := new(models.ShoppingCart)
		err := c.Bind(tmp)
		if err != nil {
			return err
		}

		user_id_int, err2 := strconv.Atoi(c.Param("user_id"))
		if err2 != nil {
			return echo.NewHTTPError(400, "User_id must be an integer")
		}
		tmp.UserID = user_id_int

		db.Create(&tmp)
		return c.JSON(201, tmp)
	})

}
