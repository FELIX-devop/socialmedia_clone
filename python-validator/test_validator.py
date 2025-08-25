#!/usr/bin/env python3
"""
Test script for the Image Validator service
"""

import requests
import os
import tempfile
from PIL import Image

def test_image_validation():
    """Test the image validation endpoint"""
    base_url = "http://localhost:5001"
    
    print("🧪 Testing Image Validator Service")
    print("=" * 40)
    
    # Test 1: Health check
    try:
        response = requests.get(f"{base_url}/health")
        if response.status_code == 200:
            print("✅ Health check passed")
        else:
            print(f"❌ Health check failed: {response.status_code}")
            return False
    except requests.exceptions.ConnectionError:
        print("❌ Cannot connect to validator service. Is it running on port 5001?")
        return False
    
    # Test 2: Valid image
    try:
        # Create a test image
        with tempfile.NamedTemporaryFile(suffix='.jpg', delete=False) as tmp_file:
            # Create a simple test image
            img = Image.new('RGB', (100, 100), color='red')
            img.save(tmp_file.name, 'JPEG')
            tmp_file.flush()
            
            # Test the validation
            with open(tmp_file.name, 'rb') as f:
                files = {'file': f}
                response = requests.post(f"{base_url}/validate", files=files)
            
            # Clean up
            os.unlink(tmp_file.name)
            
            if response.status_code == 200:
                data = response.json()
                if data.get('ok') and data.get('mime'):
                    print(f"✅ Valid image test passed: {data.get('mime')}")
                else:
                    print(f"❌ Valid image test failed: {data}")
                    return False
            else:
                print(f"❌ Valid image test failed: {response.status_code} - {response.text}")
                return False
                
    except Exception as e:
        print(f"❌ Valid image test error: {e}")
        return False
    
    # Test 3: Invalid file (text file)
    try:
        with tempfile.NamedTemporaryFile(suffix='.txt', delete=False) as tmp_file:
            tmp_file.write(b"This is not an image file")
            tmp_file.flush()
            
            with open(tmp_file.name, 'rb') as f:
                files = {'file': f}
                response = requests.post(f"{base_url}/validate", files=files)
            
            # Clean up
            os.unlink(tmp_file.name)
            
            if response.status_code == 400:
                data = response.json()
                if not data.get('ok') and data.get('error'):
                    print(f"✅ Invalid file test passed: {data.get('error')}")
                else:
                    print(f"❌ Invalid file test failed: {data}")
                    return False
            else:
                print(f"❌ Invalid file test failed: {response.status_code} - {response.text}")
                return False
                
    except Exception as e:
        print(f"❌ Invalid file test error: {e}")
        return False
    
    print("=" * 40)
    print("🎉 All tests passed! Image validator is working correctly.")
    return True

if __name__ == "__main__":
    test_image_validation()
