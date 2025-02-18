"use client";
import { useState } from "react";
import Image from "next/image";

interface wrongBoxProps {
  user: string;
  answer: string;
}

export default function WrongBox({ user, answer }: wrongBoxProps) {
  const [hovered, setHovered] = useState(false);

  // 마우스 올리면 hover 유무 true
  const handleMouseEnter = (): void => {
    setHovered(true);
  };

  // 마우스 떠나면 hover 유무 false
  const handleMouseLeave = (): void => {
    setHovered(false);
  };
  
  return (
    <div
      className={`relative p-5 text-center align-bottom cursor-pointer drop-shadow-md w-52 h-28 rounded-3xl bg-f5red-100 mb:w-36 mb:h-28 ${hovered ? "bg-f5gray-400 bg-opacity-50 transition-all ease-in-out" : ""}`}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <Image src="/images/wrong.png" alt="wrong" width={23} height={23}/>
      <div className="text-lg font-semibold text-center mb:text-base">
        {hovered ? answer : user}
      </div>
    </div>
  );
}
