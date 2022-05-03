package routes

import (
	"example/hello/database/models"

	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
)

func AddUserRoutes(e *echo.Echo, db *gorm.DB) {
	g := e.Group("/users")
	g.GET("/", func(c echo.Context) error {
		var users []models.User
		db.Find(&users)
		return c.JSON(200, users)
	})
	g.GET("/:id/", func(c echo.Context) error {
		var user models.User
		err := db.First(&user, c.Param("id")).Error
		if err != nil {
			return echo.NewHTTPError(404, "User with id: "+c.Param("id")+" not found")
		}
		return c.JSON(200, user)
	})

	g.DELETE("/:id/", func(c echo.Context) error {
		var user models.User
		err := db.First(&user, c.Param("id")).Error
		if err != nil {
			return echo.NewHTTPError(404, "User with id: "+c.Param("id")+" not found")
		}
		db.Delete(&user)
		return c.NoContent(204)
	})

	g.POST("/", func(c echo.Context) error {
		var user models.User
		err := c.Bind(&user)
		if err != nil {
			return err
		}
		return c.JSON(201, user)
	})
}
