package routes

import (
	"example/hello/database/models"

	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
)

func AddProductRoutes(e *echo.Echo, db *gorm.DB) {
	g := e.Group("/products")
	g.GET("/", func(c echo.Context) error {
		var products []models.Product
		db.Find(&products)
		return c.JSON(200, products)
	})

	g.POST("/", func(c echo.Context) error {
		product := new(models.Product)
		c.Bind(product)
		db.Create(&product)
		return c.JSON(201, product)
	})

	g.GET("/:id/", func(c echo.Context) error {
		var product models.Product
		err := db.First(&product, c.Param("id")).Error
		if err != nil {
			return echo.NewHTTPError(404, "Product with id: "+c.Param("id")+" not found")
		}
		return c.JSON(200, product)
	})

	g.DELETE("/:id/", func(c echo.Context) error {
		var product models.Product
		err := db.First(&product, c.Param("id")).Error
		if err != nil {
			return echo.NewHTTPError(404, "Product with id: "+c.Param("id")+" not found")
		}
		db.Delete(&product)
		return c.NoContent(204)
	})

}
