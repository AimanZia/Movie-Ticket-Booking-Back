import requests
import sys
from user import *
from booking import *
from wallet import *
from utils import *

# Insufficient Wallet Balance
# creates a user, credits 1000 to wallet, tries to debit 2000 from wallet

def main():
    response = delete_users()
    if not check_response_status_code(response, 200):
        return False
    
    response = delete_wallets()
    if not check_response_status_code(response, 200):
        return False
    
    name = "Aditi Pandey"
    email = "ap@iisc.ac.in"
    
    user_post_response = post_user(name, email)
    if (not test_post_user(name, email, user_post_response)):
        return False
    user = user_post_response.json()
    user_id = user['id']

    wallet_credit_response = put_wallet(user_id, "credit", 1000)
    if not test_put_wallet(user_id, "credit", 1000, 0, wallet_credit_response):
        return False
    
    wallet_get_response = get_wallet(user_id)
    if not test_get_wallet(user_id, wallet_get_response, True):
        return False

    wallet_debit_response = put_wallet(user_id, "debit", 2000)
    if not test_put_wallet(user_id, "debit", 2000, 1000, wallet_debit_response):
        return False

    wallet_get_response = get_wallet(user_id)
    if not test_get_wallet(user_id, wallet_get_response, True, 1000):
        return False

    return True
    
if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
