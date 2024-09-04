# Word Square Generator

This project is a Spring Boot application that generates word squares from a given set of random letters. A word square is a special type of word arrangement where words of equal length are placed in a square grid such that the words read the same horizontally and vertically.

## Features

- Accepts a string of random letters and returns a valid word square if one can be constructed.
- Uses a dictionary to find valid words for constructing the word square.
- Provides a RESTful API with a single POST endpoint.

## How It Works

1. **Input**: The application receives a string of random letters via a POST request.
2. **Validation**: It checks if a word square can be made using the input letters. The square's size is determined by the square root of the length of the input string.
3. **Word Filtering**: It filters words from a dictionary that match the required length and can be formed using the input letters.
4. **Square Construction**: It attempts to build a word square using the filtered words.
5. **Output**: If a valid word square is found, it returns it in JSON format. Otherwise, it returns a message indicating that no valid word square was found.

## API Endpoint

### POST `/api/word-square`

- **Request Parameter**: `letters` (string) - A string containing random letters.
- **Response**: A JSON array of words forming a valid word square, or a message indicating no valid square was found.

**Example Request:**

```http
POST http://localhost:8080/api/word-square?letters=RESIHERREAETBUNTRSNAEDMBE

### Used this website to generate the random letters: https://capitalizemytitle.com/random-letter-generator/
