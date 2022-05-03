package main

import (
	"example/hello/database"
	"example/hello/routes"
	"fmt"

	"github.com/labstack/echo/v4"
)

func main() {
	fmt.Println("Hello, World!")
	e := echo.New()

	db := database.Connect()

	e.GET("/hello", func(c echo.Context) error {
		return c.String(200, "Hello world")
	})

	routes.AddUserRoutes(e, db)
	routes.AddCategoryRoutes(e, db)
	routes.AddProductRoutes(e, db)

	e.Start(":8080")
}
