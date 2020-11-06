#include <stdio.h>
#include <string.h>
#include <assert.h>

/*created because C.atoi() processes strings not individual chars
    converts char to int, also interprets strikes and spares as ints,
    special return case of -1 for spare.
*/
int ctoi(int i, char* str){
    int ret;

    if(str[i] == 'X'){
        ret = 10;
    }else if(str[i] == '/'){
        ret = -1;
    }else{
        ret = str[i] -'0'; //converts numeric char to int
    }

    return ret;
}

//calculates a bowling score given a correctly formatted string of bowling throws
int calculateScore(char* throws){
    int frame = 0, throw1 = 0, throw2 = 0, total = 0;

    for(int i = 0; i < strlen(throws); i++){
        switch(throws[i]){
            case '-': //delimeter for bowling rounds
                frame++;
            break;

            case 'X': //calculate strike
                if(frame == 10)break; //skip 11th frame (0 indexed)

                if(throws[i+2] == 'X'){ //first throw is another strike
                    throw1 = 10;

                    if(frame==9){
                        throw2 = ctoi(i+3,throws);
                    }else{
                        throw2 = ctoi(i+4,throws);
                    }
                }else{
                    throw1 = throws[i+2] - '0';

                    throw2 = ctoi(i+3,throws);
                    if(throw2 == -1)throw2 = 10 - throw1; //if throw2 was a spare
                }

                total += 10 + throw1 + throw2;
            break;

            default: //calculate two numeric throws or spare
                if(frame == 10)break;

                if(throws[i+1] == '/'){
                    throw1 = ctoi(i+3,throws);
                    total += 10 + throw1;
                    i++;//consume '/' char
                }else{
                    throw1 = ctoi(i++,throws);
                    throw2 = ctoi(i,throws);
                    total += throw1 + throw2;
                }
            break;
        }
    }
    return total;
}

//run all tests to completion; assert will abort program upon a failed test
void unitTests(){
    printf("Running Numeric tests: ");
    //edge case: lowest possible score
    assert(calculateScore("00-00-00-00-00-00-00-00-00-00") == 0);
    //assortment of numbers
    assert(calculateScore("01-10-01-10-01-10-01-10-01-10") == 10);
    //given by specs, edge case: highest score with no strikes/spares
    assert(calculateScore("45-54-36-27-09-63-81-18-90-72") == 90);
    printf("PASSED\n");

    printf("Running Spares tests: ");
    //given by specs, edge case: 10th round spare
    assert(calculateScore("45-54-36-27-09-63-81-18-90-7/-5") == 96);
    //a few spares
    assert(calculateScore("32-71-80-4/-3/-90-53-81-18-9/-7") == 105);
    //given by specs
    assert(calculateScore("5/-5/-5/-5/-5/-5/-5/-5/-5/-5/-5") == 150);
    printf("PASSED\n");

    printf("Running Strikes tests: ");
    //consecutive strikes
    assert(calculateScore("X-X-01-11-11-11-11-11-11-11") == 46);
    //strike in 10th round
    assert(calculateScore("45-54-36-27-09-63-81-18-90-X-90") == 100);
    //strike in 10th round with spare
    assert(calculateScore("45-54-36-27-09-63-81-18-90-X-9/") == 101);
    //given by specs, edge case, highest possible score
    assert(calculateScore("X-X-X-X-X-X-X-X-X-X-XX") == 300);
    printf("PASSED\n");
}

int main(int argc, char* args[]){
    if(argc != 2){
        printf("Running unit tests...\n");
        unitTests();
    }else{
        //simply return score
        printf("%d\n",calculateScore(args[1]));
    }
    return 0;
}
