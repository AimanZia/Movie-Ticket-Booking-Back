import requests
import sys
from utils import *
from user import *
from wallet import *
from booking import *

# Create a User

def main():
    response = delete_users()
    if not check_response_status_code(response, 200):
        return False

    name = "Aditi Pandey"
    email = "ap@iisc.ac.in"

    response = post_user(name, email)
    if not test_post_user(name, email, response):
        return False

    return True

if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
