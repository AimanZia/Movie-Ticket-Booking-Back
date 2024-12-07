import requests
from utils import *

bookingServiceURL = "http://localhost:8081"

def get_show(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    print_request('GET', f'/shows/{show_id}')
    print_response(response)
    return response

def get_bookings(user_id):
    response = requests.get(bookingServiceURL + f"/bookings/users/{user_id}")
    print_request('GET', f'/bookings/users/{user_id}')
    print_response(response)
    return response

def post_booking(user_id, show_id, seats_booked):
    payload = {"show_id":show_id, "user_id":user_id, "seats_booked":seats_booked}
    response = requests.post(bookingServiceURL + "/bookings", json = payload)
    print_request('POST', f'/bookings', payload)
    print_response(response)
    return response

def delete_booking(user_id):
    response = requests.delete(bookingServiceURL + f"/bookings/users/{user_id}")
    print_request('DELETE', f'/bookings/users/{user_id}')
    print_response(response)
    return response   

def delete_show(user_id,show_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}/shows/{show_id}")
    print_request('DELETE', f'/bookings/users/{user_id}/shows/{show_id}')
    print_response(response)
    return response

def delete_bookings():
    response = requests.delete(bookingServiceURL + "/bookings")
    print_request('DELETE', f'/bookings')
    print_response(response)
    return response

def test_get_show(response):
    if not check_json_exists(response):
        return False

    payload = response.json()
    
    if not check_field_exists(payload, 'id'):
        return False

    if not check_field_type(payload, 'id', int):
        return False

    if not check_field_exists(payload, 'theatre_id'):
        return False

    if not check_field_type(payload, 'theatre_id', int):
        return False

    if not check_field_exists(payload, 'title'):
        return False

    if not check_field_type(payload, 'title', str):
        return False

    if not check_field_exists(payload, 'price'):
        return False

    if not check_field_type(payload, 'price', int):
        return False

    if not check_field_exists(payload, 'seats_available'):
        return False

    if not check_field_type(payload, 'seats_available', int):
        return False

    if not check_fields_count(payload, 5):
        return False
    
    if not check_response_status_code(response, 200):
        return False

    return True


def test_post_booking(booking_response, show_before, show_after, wallet_before, wallet_after, seats_booked):
    if not check_field_value(show_before, 'id', show_after['id']):
        return False

    price = show_before['price']
    seats_available = show_before['seats_available']
    balance_available = wallet_before['balance']

    can_book = seats_available >= seats_booked and balance_available >= (seats_booked * price)

    if not can_book:
        if not check_field_value(show_after, 'seats_available', seats_available):
            return False

        if not check_field_value(wallet_after, 'balance', balance_available):
            return False

        if not check_response_status_code(booking_response, 400):
            return False

        return True
    else:
        if not check_field_value(show_after, 'seats_available', seats_available - seats_booked):
            return False

        if not check_field_value(wallet_after, 'balance', balance_available - price * seats_booked):
            return False

        if not check_response_status_code(booking_response, 200):
            return False

        return True
    
        
            
