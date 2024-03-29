"use client";

import Image from "next/image";
import useSearchStore from "@/store/searchStore";
import { techDataMap } from "@/data/techData";
import { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import TanstackProvider from "@/providers/TanstackProvider";
import { techTypes } from "@/data/techData";
import { useMediaQuery } from "react-responsive";
import { debounce } from "@/data/functions";

export default function RecruitLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const { setKeywords, setQuery } = useSearchStore();

  const [nowType, setNowType] = useState("언어");
  const [pickTechList, setPickTechList] = useState<string[]>([]);
  const [techs, setTechs] = useState<string[]>(techDataMap.get("언어") ?? []);
  const isMobile = useMediaQuery({
    query: "(max-width:480px)",
  });

  const changeTechTypeHandler = (techType: string) => {
    setNowType(techType);
    let techsTmp: string[] = [...(techDataMap.get(techType) || [])];
    pickTechList.forEach((s) => {
      const index: number = techsTmp?.indexOf(s) ?? -1;
      if (index !== -1) {
        techsTmp?.splice(index, 1);
      }
    });
    setTechs(techsTmp);
  }; // 테크타입 설정

  const techClickHandler = (tech: string) => {
    setPickTechList([...pickTechList, tech]);
    const index: number = techs?.indexOf(tech) ?? -1;
    if (index !== -1) {
      techs?.splice(index, 1);
    }
  }; //클릭한거 pickTechList에 업데이트 밑 techs에서는 빼기

  const techDeleteHandler = (tech: string) => {
    setPickTechList((prevTechList) =>
      prevTechList.filter((item) => item !== tech)
    );
  }; // 골라놓은 내 테크로고 중 클릭한거 없애기

  useEffect(() => {
    let techsTmp: string[] = [...(techDataMap.get(nowType) || [])] ?? [];
    pickTechList.forEach((s) => {
      const index: number = techsTmp?.indexOf(s) ?? -1;
      if (index !== -1) {
        techsTmp?.splice(index, 1);
      }
    });
    setTechs(techsTmp);
    setKeywords(pickTechList);
  }, [pickTechList, nowType, setKeywords]); // 테크타입 바꾸면 보여지는 거 설정 로직 이미 뽑혀잇는거면 뺀다음에 보여준다.

  return (
    <div className={`flex ${isMobile ? "flex-col" : ""}  mx-10 my-5`}>
      <div className="min-w-[330px] max-w-[330px] my-5 min-h-96">
        <div className="flex w-full justify-center my-3 items-center gap-2 bg-f5gray-300 h-10 rounded-md text-f5gray-500 text-sm p-2">
          <input
            type="text"
            placeholder="검색어를 입력해주세요"
            className="flex-1 outline-none bg-f5gray-300"
            onChange={debounce((event) => setQuery(event.target.value), 1000)}
          />
          <span>
            <FaSearch />
          </span>
        </div>
        <div className="flex flex-wrap gap-x-2 gap-y-1 min-h-8">
          {pickTechList.map((pickTech: string, index: number) => {
            return (
              <button
                type="button"
                onClick={() => techDeleteHandler(pickTech)}
                key={index}
                className="p-1 text-sm border border-f5gray-300 rounded-2xl hover:bg-f5red-200 "
              >
                <Image
                  src={`/images/techLogo/${pickTech}.png`}
                  alt={pickTech}
                  width="20"
                  height="20"
                  className="w-5 h-5"
                />
              </button>
            );
          })}
        </div>
        <div className="flex flex-wrap gap-x-4 gap-2 mt-3">
          {techTypes.map((techType: string, index: number) => {
            const isActive: boolean = nowType == techType;
            return (
              <button
                type="button"
                onClick={(): void => changeTechTypeHandler(techType)}
                className={`border border-f5gray-300 rounded-3xl p-2 my-0 hover:bg-f5green-200 text-sm ${isActive ? "border-f5green-400" : ""}`}
                key={index}
              >
                {techType}
              </button>
            );
          })}
        </div>
        <div className="flex flex-wrap gap-x-4 gap-y-2 mt-3 max-w-[1000px]">
          {techs?.map((tech: string, index: number) => {
            return (
              <button
                type="button"
                key={index}
                onClick={() => techClickHandler(tech)}
                className="flex flex-row items-center p-1 text-sm border border-f5gray-300 rounded-2xl hover:bg-f5green-200"
              >
                <Image
                  src={`/images/techLogo/${tech}.png`}
                  alt={tech}
                  width="28"
                  height="28"
                  className="w-auto"
                />
              </button>
            );
          })}
        </div>
      </div>
      <TanstackProvider>
        <div className={`flex-grow ${isMobile ? "" : "ml-5"} `}>{children}</div>
      </TanstackProvider>
    </div>
  );
}
