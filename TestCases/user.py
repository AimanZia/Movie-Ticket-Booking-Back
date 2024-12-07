import requests
from utils import *

userServiceURL = "http://localhost:8080"

def post_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    print_request('POST', '/users', new_user)
    print_response(response)
    return response

def get_user(user_id):
    response = requests.get(f"{userServiceURL}/users/{user_id}")
    print_request('GET', f'/users/{user_id}')
    print_response(response)
    return response

def delete_user(user_id):
    response = requests.delete(f"{userServiceURL}/users/{user_id}")
    print_request('DELETE', f'/users/{user_id}')
    print_response(response)
    return response

def delete_users():
    response = requests.delete(userServiceURL + "/users")
    print_request('DELETE', f'/users')
    print_response(response)
    return response

def test_post_user(name, email, response):
    if not check_json_exists(response):
        return False
    
    payload = response.json()
    
    if not check_field_exists(payload, 'id'):
        return False

    if not check_field_type(payload, 'id', int):
        return False

    if not check_field_exists(payload, 'name'):
        return False
    
    if not check_field_type(payload, 'name', str):
        return False
    
    if not check_field_value(payload, 'name', name):
        return False
    
    if not check_field_exists(payload, 'email'):
        return False
    
    if not check_field_type(payload, 'email', str):
        return False

    if not check_field_value(payload, 'email', email):
        return False
    
    if not check_fields_count(payload, 3):
        return False
    
    if not check_response_status_code(response, 201):
        return False

    return True

def test_get_user(user_id, response, exists):
    if exists:
        if not check_json_exists(response):
            return False
        
        payload = response.json()

        if not check_field_exists(payload, 'id'):
            return False

        if not check_field_type(payload, 'id', int):
            return False

        if not check_field_value(payload, 'id', user_id):
            return False

        if not check_field_exists(payload, 'name'):
            return False

        if not check_field_type(payload, 'name', str):
            return False

        if not check_field_exists(payload, 'email'):
            return False

        if not check_field_type(payload, 'email', str):
            return False

        if not check_fields_count(payload, 3):
            return False
    
        if not check_response_status_code(response, 200):
            return False

        return True
    else:
        if not check_response_status_code(response, 404):
            return False

        return True

def test_delete_user(user_id, response, exists):
    if exists:
        if not check_response_status_code(response, 200):
            return False

        return True
    else:
        if not check_response_status_code(response, 404):
            return False

        return True
