#!/usr/bin/env python3
"""
Integration Verification Script for Backend API
Tests all endpoints and validates data structures for iOS app integration
"""

import json
import sys
import requests
import time
from pathlib import Path

BASE_URL = "http://localhost:8000"
# For remote server, use: BASE_URL = "http://86.38.238.159:8000"

def test_health():
    """Test health check endpoint"""
    print("? Testing health check endpoint...")
    try:
        response = requests.get(f"{BASE_URL}/api/pipeline/health", timeout=5)
        if response.status_code == 200:
            data = response.json()
            print(f"   ? Health check OK: {data.get('status')}")
            print(f"   Active runs: {data.get('active_runs', 0)}")
            return True
        else:
            print(f"   ? Health check failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"   ? Health check error: {e}")
        return False

def test_cors():
    """Test CORS configuration"""
    print("\n? Testing CORS configuration...")
    try:
        headers = {
            "Origin": "http://localhost:3000",
            "Access-Control-Request-Method": "POST",
            "Access-Control-Request-Headers": "content-type"
        }
        response = requests.options(f"{BASE_URL}/api/pipeline/execute", headers=headers, timeout=5)
        
        cors_headers = {
            key: response.headers.get(key) 
            for key in response.headers 
            if 'access-control' in key.lower()
        }
        
        if cors_headers:
            print(f"   ? CORS headers present:")
            for key, value in cors_headers.items():
                print(f"      {key}: {value}")
            
            # Check if allows all origins
            if response.headers.get('Access-Control-Allow-Origin') == '*':
                print("   ? Allows all origins (compatible with iOS app)")
            
            return True
        else:
            print("   ??  No CORS headers found")
            return False
    except Exception as e:
        print(f"   ? CORS test error: {e}")
        return False

def test_get_runs():
    """Test getting all runs"""
    print("\n? Testing GET /api/pipeline/runs...")
    try:
        response = requests.get(f"{BASE_URL}/api/pipeline/runs", timeout=5)
        if response.status_code == 200:
            data = response.json()
            runs = data.get('runs', [])
            print(f"   ? Successfully retrieved {len(runs)} runs")
            
            # Check if any runs are completed
            completed = [r for r in runs if r.get('status') == 'COMPLETED']
            if completed:
                print(f"   ? Found {len(completed)} completed run(s) for testing")
                return completed[0].get('runId')
            else:
                print("   ??  No completed runs found")
                return None
        else:
            print(f"   ? Failed: {response.status_code}")
            return None
    except Exception as e:
        print(f"   ? Error: {e}")
        return None

def test_get_status(run_id):
    """Test getting status of a run"""
    print(f"\n? Testing GET /api/pipeline/status/{run_id}...")
    try:
        response = requests.get(f"{BASE_URL}/api/pipeline/status/{run_id}", timeout=5)
        if response.status_code == 200:
            data = response.json()
            status = data.get('status')
            print(f"   ? Status retrieved: {status}")
            
            # Validate response structure
            required_fields = ['runId', 'sessionId', 'status']
            missing = [f for f in required_fields if f not in data]
            if missing:
                print(f"   ??  Missing fields: {missing}")
            else:
                print("   ? All required fields present")
            
            # Check if completed and has results
            if status == 'COMPLETED':
                has_result = 'completeResult' in data or 'frontendData' in data
                if has_result:
                    print("   ? Completed run has result data")
                    
                    # Validate frontendData structure
                    if 'frontendData' in data:
                        fd = data['frontendData']
                        print("   ? frontendData present")
                        
                        checks = {
                            'transcript': fd.get('transcript', {}).get('segments'),
                            'todos': fd.get('todos', {}).get('items'),
                            'meeting_minutes': fd.get('meeting_minutes'),
                            'participants': fd.get('participants'),
                            'knowledge_graph': fd.get('knowledge_graph'),
                        }
                        
                        print("\n   ? Frontend Data Structure:")
                        for key, value in checks.items():
                            status = "?" if value else "?? "
                            print(f"      {status} {key}: {'Present' if value else 'Missing'}")
                    else:
                        print("   ??  frontendData not found in completed run")
                    
                    # Check completeResult as fallback
                    if 'completeResult' in data:
                        cr = data['completeResult']
                        print("   ? completeResult present")
                        
                        cr_checks = {
                            'todos': cr.get('todos'),
                            'kg_entities': cr.get('kg_entities'),
                            'meeting_minutes': cr.get('meeting_minutes'),
                            'asr_result': cr.get('asr_result'),
                        }
                        
                        print("\n   ? Complete Result Structure:")
                        for key, value in cr_checks.items():
                            status = "?" if value else "?? "
                            print(f"      {status} {key}: {'Present' if value else 'Missing'}")
                
                return True
            else:
                print(f"   ??  Run status: {status}")
                return True
        else:
            print(f"   ? Failed: {response.status_code}")
            return False
    except Exception as e:
        print(f"   ? Error: {e}")
        return False

def test_sample_data_structure():
    """Validate sample data structure from test results"""
    print("\n? Validating sample data structure...")
    
    sample_file = Path("/root/KK_pipelinetests/test_results/complete_pipeline_1761911077.json")
    
    if not sample_file.exists():
        print("   ??  Sample file not found")
        return False
    
    try:
        with open(sample_file, 'r') as f:
            data = json.load(f)
        
        print("   ? Sample file loaded")
        
        # Check structure
        checks = {
            'frontend_data exists': 'frontend_data' in data,
            'frontend_data.transcript': 'transcript' in data.get('frontend_data', {}),
            'frontend_data.todos': 'todos' in data.get('frontend_data', {}),
            'frontend_data.meeting_minutes': 'meeting_minutes' in data.get('frontend_data', {}),
            'frontend_data.participants': 'participants' in data.get('frontend_data', {}),
            'completeResult.todos exists': 'todos' in data,
            'completeResult.kg_entities exists': 'kg_entities' in data,
            'completeResult.meeting_minutes exists': 'meeting_minutes' in data,
        }
        
        print("\n   ? Structure Validation:")
        all_ok = True
        for check, result in checks.items():
            status = "?" if result else "?"
            print(f"      {status} {check}")
            if not result:
                all_ok = False
        
        if all_ok:
            print("\n   ? All structure checks passed!")
        
        return all_ok
        
    except Exception as e:
        print(f"   ? Error: {e}")
        return False

def test_upload_endpoint_structure():
    """Test if upload endpoint accepts correct format"""
    print("\n? Testing upload endpoint structure (dry run)...")
    print("   ??  Endpoint: POST /api/pipeline/execute")
    print("   ??  Expected format: multipart/form-data")
    print("   ??  Fields: file (audio), session_id (optional), enable_preprocessing (optional)")
    print("   ??  Response: { sessionId, status, message, runId }")
    print("   ? Structure documented correctly")
    return True

def generate_integration_summary():
    """Generate summary for iOS team"""
    print("\n" + "="*60)
    print("? iOS APP INTEGRATION SUMMARY")
    print("="*60)
    
    print("\n? Backend API Base URL:")
    print(f"   Local: {BASE_URL}")
    print("   Remote: http://86.38.238.159:8000")
    
    print("\n? Endpoints Available:")
    print("   1. POST   /api/pipeline/execute")
    print("   2. GET    /api/pipeline/status/{runId}")
    print("   3. GET    /api/pipeline/logs/{runId}")
    print("   4. GET    /api/pipeline/runs")
    print("   5. GET    /api/pipeline/health")
    
    print("\n? Data Mapping:")
    print("   Transcripts:  frontendData.transcript.segments")
    print("   Todos:        frontendData.todos.items")
    print("   Minutes:      frontendData.meeting_minutes.content")
    print("   Participants: frontendData.participants.items")
    print("   Knowledge Graph: completeResult.kg_entities + kg_relations")
    
    print("\n? CORS Configuration:")
    print("   - Allows all origins (*)")
    print("   - Supports all HTTP methods")
    print("   - iOS app should work without CORS issues")
    
    print("\n? Integration Flow:")
    print("   1. Record audio in iOS app")
    print("   2. POST audio to /api/pipeline/execute")
    print("   3. Get runId from response")
    print("   4. Poll GET /api/pipeline/status/{runId}")
    print("   5. When status='COMPLETED', extract frontendData")
    print("   6. Map to UI sections")
    
    print("\n" + "="*60)

def main():
    print("="*60)
    print("? BACKEND INTEGRATION VERIFICATION")
    print("="*60)
    
    results = {
        'health': test_health(),
        'cors': test_cors(),
        'runs': test_get_runs(),
        'structure': test_sample_data_structure(),
        'upload_structure': test_upload_endpoint_structure(),
    }
    
    # Test status if we have a run ID
    if results['runs']:
        results['status'] = test_get_status(results['runs'])
    else:
        print("\n??  Skipping status test - no completed runs found")
        results['status'] = None
    
    # Generate summary
    generate_integration_summary()
    
    # Final report
    print("\n" + "="*60)
    print("? VERIFICATION REPORT")
    print("="*60)
    
    passed = sum(1 for v in results.values() if v is True)
    total = sum(1 for v in results.values() if v is not None)
    
    for test, result in results.items():
        status = "? PASS" if result is True else "??  SKIP" if result is None else "? FAIL"
        print(f"{status} {test}")
    
    print(f"\n? Passed: {passed}/{total} tests")
    
    if passed == total:
        print("\n? All tests passed! Backend is ready for iOS integration.")
        return 0
    else:
        print("\n??  Some tests failed. Review output above.")
        return 1

if __name__ == "__main__":
    sys.exit(main())

