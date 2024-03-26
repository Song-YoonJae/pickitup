"use client";
import { useState, useEffect } from "react";
import Image from "next/image";

import Realistic from "../../realistic";
import WrongBox from "./wrongBox";
import RightBox from "./rightBox";
import useAuthStore, { AuthState } from "@/store/authStore";

interface Answer {
  question: string;
  answer: string;
  user: string;
  correct: boolean;
  index: number;
}

interface QuizResultProps {
  answer: Answer[];
}

export default function QuizResult({ answer }: QuizResultProps) {
  const [showConfetti, setShowConfetti] = useState(false);
  const apiURL = "https://spring.pickITup.online/quizzes/win";
  const isLoggedIn: boolean = useAuthStore(
    (state: AuthState) => state.isLoggedIn
  );

  // 7문제 이상 정답 시 뱃지 획득을 위한 승리횟수 1 증가
  const addWinNumber = async () => {
    if (isLoggedIn) {
      const accessToken = sessionStorage.getItem("accessToken");
      try {
        await fetch(apiURL, {
          method: "PATCH",
          headers: {
            Authorization: "Bearer " + accessToken,
          },
        });
      } catch (error) {
        console.log(error);
      }
    } else {
      alert("게임 결과를 저장하기 위해서 로그인이 필요합니다!");
    }
  };


  useEffect(() => {
    const correctCount = answer.filter((e: Answer) => e.correct).length;

    if (correctCount >= 7) {
      setShowConfetti(true);
      addWinNumber();
    }
  }, [answer]);
  
  return (
    <div>
      <div className="flex flex-wrap items-center justify-center">
        <div className="flex flex-col mx-1 ml-10">
          <div className="flex flex-wrap justify-center my-10 text-4xl font-semibold tracking-widest">
            <div className="mr-3 text-f5green-300">게임</div>
            <div className="text-f5black-400">결과</div>
          </div>
          <div className="text-xs text-f5black-400"></div>
        </div>
        <Image
          src="/images/hourglass2.png"
          alt="gameMachine"
          width={110}
          height={110}
          priority={true}
        />
      </div>
      <div className="flex justify-center">
        <span className="flex items-center justify-center h-16 rounded-full w-28 bg-f5gray-300">
          <b className="text-3xl">
            {answer.filter((e: Answer): boolean => e.correct).length}
          </b>
          &nbsp;&nbsp;/&nbsp;&nbsp;10
        </span>
      </div>
      <div className="mt-12 mx-28">
        <div className="flex justify-around">
          {answer.slice(0, 5).map((e: Answer, idx: number) => (
            <div key={idx}>
              {e.correct ? (
                <RightBox answer={e.answer} />
              ) : (
                <WrongBox user={e.user} answer={e.answer} />
              )}
            </div>
          ))}
        </div>
        <div className="flex justify-around mt-8">
          {answer.slice(5, 10).map((e: Answer, idx: number) => (
            <div key={idx}>
              {e.correct ? (
                <RightBox answer={e.answer} />
              ) : (
                <WrongBox user={e.user} answer={e.answer} />
              )}
            </div>
          ))}
        </div>
      </div>
      {showConfetti && <Realistic />}
    </div>
  );
}
