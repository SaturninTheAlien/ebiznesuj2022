package routes

import (
	"example/hello/database/models"

	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
)

func AddCategoryRoutes(e *echo.Echo, db *gorm.DB) {
	g := e.Group("/category")
	g.GET("/", func(c echo.Context) error {
		var categories []models.ProductCategory
		db.Find(&categories)
		return c.JSON(200, categories)
	})

	g.GET("/:id/", func(c echo.Context) error {
		var category models.ProductCategory
		err := db.First(&category, c.Param("id")).Error
		if err != nil {
			return echo.NewHTTPError(404, "Category with id "+c.Param("id")+" not found")
		}
		return c.JSON(200, category)
	})

	g.POST("/", func(c echo.Context) error {
		category := new(models.ProductCategory)
		err := c.Bind(category)
		if err != nil {
			return err
		}
		db.Create(&category)
		return c.JSON(201, category)
	})

	g.DELETE("/:id/", func(c echo.Context) error {
		var category models.ProductCategory
		err := db.First(&category, c.Param("id")).Error
		if err != nil {
			return echo.NewHTTPError(404, "Category with id "+c.Param("id")+" not found")
		}
		db.Delete(&category)
		return c.NoContent(204)
	})
}
