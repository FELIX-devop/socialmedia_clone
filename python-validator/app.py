from flask import Flask, request, jsonify
from flask_cors import CORS
from PIL import Image
import io
import os

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

@app.route('/validate', methods=['POST'])
def validate_image():
    """
    Validate uploaded file to ensure it's a valid image
    """
    try:
        # Check if file is present in request
        if 'file' not in request.files:
            print("error: no file provided")
            return jsonify({
                "ok": False,
                "error": "No file provided"
            }), 400
        
        file = request.files['file']
        
        # Check if file has a name
        if file.filename == '':
            print("error: no file selected")
            return jsonify({
                "ok": False,
                "error": "No file selected"
            }), 400
        
        # Check if file is empty
        if file.content_length == 0:
            print("error: empty file")
            return jsonify({
                "ok": False,
                "error": "Empty file"
            }), 400
        
        # Try to open and validate the image using Pillow
        try:
            # Read file content
            file_content = file.read()
            file.seek(0)  # Reset file pointer for future reads
            
            # Open image with Pillow
            img = Image.open(io.BytesIO(file_content))
            
            # Verify the image (this will raise an exception if image is corrupted)
            img.verify()
            
            # Get image format
            img_format = img.format
            mime_type = f"image/{img_format.lower()}"
            
            print("it is image file")
            
            return jsonify({
                "ok": True,
                "format": img_format,
                "mime": mime_type
            })
            
        except Exception as e:
            print(f"error: not an image - {str(e)}")
            return jsonify({
                "ok": False,
                "error": "Invalid image file"
            }), 400
            
    except Exception as e:
        print(f"error: unexpected error - {str(e)}")
        return jsonify({
            "ok": False,
            "error": "Unexpected error occurred"
        }), 500

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({"status": "healthy", "service": "image-validator"})

if __name__ == '__main__':
    print("Starting Image Validator Service on port 5001...")
    print("Endpoint: POST /validate")
    print("Health check: GET /health")
    app.run(host='0.0.0.0', port=5001, debug=True)
