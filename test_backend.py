#!/usr/bin/env python3
"""
Backend API Testing Script
Tests each endpoint independently to identify breaking points
"""

import requests
import json
import time
import sys
from pathlib import Path

# Configuration
BASE_URL = "http://localhost:8080"
TEST_CSV = "test_data_samples.csv"

def print_header(test_name):
    print(f"\n{'='*50}")
    print(f"TEST: {test_name}")
    print(f"{'='*50}")

def print_result(status, message):
    if status == "PASS":
        print(f"PASS: {message}")
    else:
        print(f"FAIL: {message}")

def test_backend_connection():
    """Test if backend is running"""
    print_header("Backend Connection Test")
    
    try:
        response = requests.get(f"{BASE_URL}/", timeout=5)
        # Spring Boot usually returns 404 for root path but that means server is running
        if response.status_code in [200, 404]:
            print_result("PASS", f"Backend is running (status: {response.status_code})")
            return True
        else:
            print_result("FAIL", f"Unexpected status: {response.status_code}")
            return False
    except requests.exceptions.ConnectionError:
        print_result("FAIL", "Cannot connect to backend - is it running on port 8080?")
        return False
    except requests.exceptions.Timeout:
        print_result("FAIL", "Connection timeout")
        return False
    except Exception as e:
        print_result("FAIL", f"Connection error: {e}")
        return False

def test_csv_upload():
    """Test CSV upload endpoint"""
    print_header("CSV Upload Test")
    
    # Check if test CSV exists
    csv_path = Path(TEST_CSV)
    if not csv_path.exists():
        print_result("FAIL", f"Test CSV file not found: {TEST_CSV}")
        return False
    
    try:
        with open(csv_path, 'rb') as f:
            files = {'file': (TEST_CSV, f, 'text/csv')}
            response = requests.post(f"{BASE_URL}/upload", files=files, timeout=10)
        
        print(f"Status Code: {response.status_code}")
        print(f"Response: {response.text}")
        
        if response.status_code == 200:
            print_result("PASS", "CSV upload successful")
            return True
        else:
            print_result("FAIL", f"Upload failed with status {response.status_code}")
            return False
            
    except Exception as e:
        print_result("FAIL", f"Upload error: {e}")
        return False

def test_chart_endpoint():
    """Test chart data generation endpoint"""
    print_header("Chart Data Generation Test")
    
    try:
        response = requests.get(f"{BASE_URL}/chart", timeout=10)
        
        print(f"Status Code: {response.status_code}")
        print(f"Response Headers: {dict(response.headers)}")
        
        if response.status_code == 200:
            try:
                data = response.json()
                print(f"Chart Data: {json.dumps(data, indent=2)}")
                
                # Validate chart data structure
                if 'labels' in data and 'values' in data:
                    labels = data['labels']
                    values = data['values']
                    
                    if isinstance(labels, list) and isinstance(values, list):
                        print(f"Labels count: {len(labels)}")
                        print(f"Values count: {len(values)}")
                        
                        if len(labels) > 0 and len(values) > 0:
                            print_result("PASS", f"Chart data generated with {len(labels)} data points")
                            
                            # Show sample data
                            for i in range(min(3, len(labels))):
                                print(f"  Sample {i+1}: {labels[i]} = {values[i]}")
                            return True
                        else:
                            print_result("FAIL", "Chart data is empty")
                            return False
                    else:
                        print_result("FAIL", "Labels or values are not arrays")
                        return False
                else:
                    print_result("FAIL", "Missing labels or values in response")
                    return False
                    
            except json.JSONDecodeError:
                print_result("FAIL", "Response is not valid JSON")
                print(f"Raw response: {response.text}")
                return False
        else:
            print_result("FAIL", f"Chart endpoint failed with status {response.status_code}")
            print(f"Error response: {response.text}")
            return False
            
    except Exception as e:
        print_result("FAIL", f"Chart endpoint error: {e}")
        return False

def test_ai_query_endpoint():
    """Test AI analysis endpoint"""
    print_header("AI Analysis Test")
    
    try:
        response = requests.get(f"{BASE_URL}/query", timeout=30)  # Longer timeout for AI
        
        print(f"Status Code: {response.status_code}")
        print(f"Response: {response.text}")
        
        if response.status_code == 200:
            response_text = response.text.strip()
            
            if response_text and len(response_text) > 10:
                print_result("PASS", f"AI analysis successful ({len(response_text)} chars)")
                print(f"Analysis preview: {response_text[:200]}...")
                return True
            else:
                print_result("FAIL", "AI analysis returned empty or very short response")
                return False
        else:
            print_result("FAIL", f"AI query failed with status {response.status_code}")
            return False
            
    except requests.exceptions.Timeout:
        print_result("FAIL", "AI query timeout (this may indicate OpenAI API issues)")
        return False
    except Exception as e:
        print_result("FAIL", f"AI query error: {e}")
        return False

def test_complete_flow():
    """Test the complete data flow"""
    print_header("Complete Data Flow Test")
    
    # Step 1: Upload CSV
    print("Step 1: Uploading CSV...")
    if not test_csv_upload():
        return False
    
    # Wait a moment for data processing
    time.sleep(1)
    
    # Step 2: Generate chart data
    print("\nStep 2: Generating chart data...")
    if not test_chart_endpoint():
        return False
    
    # Step 3: Get AI analysis
    print("\nStep 3: Getting AI analysis...")
    if not test_ai_query_endpoint():
        return False
    
    print_result("PASS", "Complete data flow successful!")
    return True

def main():
    print("AI Data Dashboard - Backend API Testing")
    print("=" * 50)
    
    # Run all tests
    tests = [
        ("Backend Connection", test_backend_connection),
        ("CSV Upload", test_csv_upload),
        ("Chart Generation", test_chart_endpoint),
        ("AI Analysis", test_ai_query_endpoint),
        ("Complete Flow", test_complete_flow)
    ]
    
    results = {}
    
    for test_name, test_func in tests:
        try:
            results[test_name] = test_func()
        except Exception as e:
            print(f"ERROR in {test_name}: {e}")
            results[test_name] = False
    
    # Summary
    print("\n" + "=" * 50)
    print("TEST SUMMARY")
    print("=" * 50)
    
    passed = 0
    total = len(results)
    
    for test_name, result in results.items():
        status = "PASS" if result else "FAIL"
        print(f"{test_name}: {status}")
        if result:
            passed += 1
    
    print(f"\nOverall: {passed}/{total} tests passed")
    
    if passed == total:
        print("All tests passed! Backend is working correctly.")
        return 0
    else:
        print("Some tests failed. Check the detailed output above.")
        return 1

if __name__ == "__main__":
    sys.exit(main())
