"use client";
import { useState } from "react";
import { useMediaQuery } from "react-responsive";
import Image from "next/image";
import Link from "next/link";

import useAuthStore, { AuthState } from "@/store/authStore";
import { techDataMap } from "@/data/techData";
import LoginNeed from "@/components/loginNeed";

const techTypes: string[] = [
  "언어",
  "프론트엔드",
  "백엔드",
  "모바일",
  "데이터",
  "데브옵스",
  "테스팅툴",
];

export default function InterViewPage() {
  const isMobile = useMediaQuery({
    query: "(max-width:480px)",
  });

  const isLoggedIn: boolean = useAuthStore(
    (state: AuthState) => state.isLoggedIn
  );

  const [pickType, setPickType] = useState("언어");
  const [pickTech, setPickTech] = useState("");

  // 기술스택 선택 함수
  const techClickHandler = (tech: string): void => {
    setPickTech(tech);
  };

  const techs: string[] | undefined = techDataMap.get(pickType);

  return (
    isLoggedIn ? (
      <div>
        <div className="flex flex-wrap justify-center mx-auto mt-4 mb-4 mb:mt-16">
          <div className="flex flex-col justify-evenly">
            <div className="flex items-center justify-center text-4xl mb:text-3xl font-semibold tracking-wider ml-10">
              <div className="my-2 mr-1 text-f5green-300">면접 대비 </div>
              <div className="my-2 ml-2 text-f5black-400">QUIZ</div>
              {isMobile ? (
                <Image
                  src="/images/pencil.png"
                  alt="pencil"
                  width={80}
                  height={80}
                  priority={true}
                />
              ) : (
                <Image
                  src="/images/pencil.png"
                  alt="pencil"
                  width={120}
                  height={120}
                  priority={true}
                />
              )}
            </div>
            <div className="flex flex-col items-center justify-center text-sm mb:text-xs mb:mt-2">
              <div className="font-medium text-f5black-400">
                분야별로 면접 빈출 질문을 제공합니다!
              </div>
              <div className="font-medium text-f5black-400">
                시간 내에 답변을 생각하는 연습과 예시답변을 참고하여 면접을
                대비해보세요
              </div>
            </div>
          </div>
        </div>
        <div className="flex flex-col flex-wrap justify-center mb:mt-12">
          <div className="flex flex-wrap justify-center gap-2 mt-3 mb:px-10">
            {techTypes.map((techType: string, index: number) => {
              const isActive: boolean = pickType == techType;
              return (
                <button
                  type="button"
                  onClick={(): void => setPickType(techType)}
                  className={`border border-f5gray-300 rounded-2xl text-f5black-400 text-xs p-2 mb:p-1 transition-all ease-in duration-150 hover:scale-105 ${isActive ? "border-f5green-300 border-2 scale-105" : ""}`}
                  key={index}
                >
                  {techType}
                </button>
              );
            })}
          </div>
          <div className="w-1/2 mb:w-2/3 mx-auto my-4 border-t-2"></div>
          <div className="min-h-[200px] mb:min-w-[260px]">
            <div className="flex flex-wrap justify-center w-1/2 mb:w-2/3 gap-4 mb:gap-2 mx-auto">
              {techs?.map((tech: string, index: number) => {
                const techWithoutSpaces = tech.replace(/\s/g, ""); // 공백 제거
                const isActive: boolean = pickTech == tech;
                return (
                  <button
                    type="button"
                    key={index}
                    onClick={() => techClickHandler(tech)}
                    className={`flex flex-row border-f5gray-300 border py-1 pr-2 rounded-2xl text-f5black-400 text-xs items-center  hover:transition-all hover:scale-105 hover:ease-in-out  ${isActive ? "border-f5green-300 border-2 scale-105" : ""}`}
                  >
                    <Image
                      src={`/images/techLogo/${techWithoutSpaces}.png`}
                      alt={tech}
                      width={22}
                      height={22}
                      className="mx-1"
                    />
                    {tech}
                  </button>
                );
              })}
            </div>
          </div>
        </div>
        <div className="flex justify-center my-5">
          <Link href={`/main/interview/${pickTech}`}>
            <button className="px-12 py-2 text-sm font-semibold rounded-md text-neutral-100 bg-f5green-350 hover:bg-f5green-300 ring-1 ring-inset ring-f5green-700/10">
              시작하기
            </button>
          </Link>
        </div>
      </div>
    ) : (
    <LoginNeed />
    )
  );
}
