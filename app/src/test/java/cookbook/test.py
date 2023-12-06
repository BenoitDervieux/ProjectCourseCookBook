import mysql.connector

# Establish a connection to the Google Cloud SQL database
cnx = mysql.connector.connect(
    host='34.88.207.9',
    user='root',
    password='Kevin_mathwiz!23',
    database='chanuka1'
)

# Create a cursor to execute SQL queries
cursor = cnx.cursor()

for i in range(44):
    query = f"UPDATE chanuka1.recipes SET rating = '{i % 5}' WHERE recipe_id = '{i}'"

    # Execute the update query
    cursor.execute(query)
    cnx.commit()

# Close the cursor and connection
cursor.close()
cnx.close()
