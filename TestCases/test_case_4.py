import requests
import sys
from utils import *
from user import *
from wallet import *
from booking import *

# User Deletion
# creates a booking, deletes the user, checks if wallet's balance and show's
# available seats are returned to original values

def main():
    response = delete_users()
    if not check_response_status_code(response, 200):
        return False
    
    response = delete_wallets()
    if not check_response_status_code(response, 200):
        return False
    
    name = "Aditi Pandey"
    email = "ap@iisc.ac.in"
    show_id = 1
    seats_booked = 5

    user_post_response = post_user(name, email)
    if not test_post_user(name, email, user_post_response):
        return False
    user = user_post_response.json()
    user_id = user['id']

    wallet_response_before_booking = put_wallet(user_id, "credit", 1000)
    if not test_put_wallet(user_id, "credit", 1000, 0, wallet_response_before_booking):
        return False
    wallet_before = wallet_response_before_booking.json()
    
    show_details_before_booking = get_show(show_id)
    if not test_get_show(show_details_before_booking):
        return False
    show_before = show_details_before_booking.json()
    
    booking_response = post_booking(user_id, show_id, seats_booked)
    show_details_after_booking = get_show(show_id)
    show_after = show_details_after_booking.json()

    wallet_response_after_booking = get_wallet(user_id)
    if not test_get_wallet(user_id, wallet_response_after_booking, True):
        return False
    wallet_after = wallet_response_after_booking.json()
    
    if not test_post_booking(booking_response, show_before, show_after, wallet_before, wallet_after, seats_booked):
        return False
    
    delete_user_response = delete_user(user_id)
    if not test_delete_user(user_id, delete_user_response, True):
        return False

    user_response_after_deletion = get_user(user_id)
    if not test_get_user(user_id, user_response_after_deletion, False):
        return False

    wallet_response_after_deletion = get_wallet(user_id)
    if not test_get_wallet(user_id, wallet_response_after_deletion, False):
        return False

    show_details_after_deletion = get_show(show_id)
    show_after_deletion = show_details_after_deletion.json()
    if not check_field_value(show_after_deletion, 'seats_available', show_before['seats_available']):
        return False

    return True

if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
