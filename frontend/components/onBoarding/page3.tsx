import Image from "next/image";

export default function Page3({ activePage }: { activePage: boolean }) {
  return (
    <div className="w-[100%] h-[100%]">
      <div className="flex flex-wrap justify-start align-middle pt-28 pl-52">
        <div className="flex flex-col justify-evenly">
          <div className={`flex flex-col justify-start text-4xl font-semibold tracking-widest ${activePage ? "animate-slide-up" : ""}`}>
            <div className="flex">
              <div className="my-1 ml-3 text-transparent bg-clip-text bg-gradient-to-r from-f5yellowgreen-200 to-f5green-300">
                선호하는 기술 스택
              </div>
              <div className="my-1 text-f5black-400">에 맞춰</div>
            </div>
            <div className="my-1 ml-3 text-f5black-400">
              사용자 맞춤 채용 공고를 추천합니다
            </div>
          </div>
          <Image
            src="/images/companyLogo.png"
            alt="companyLogo"
            width={300}
            height={202}
            className="ml-20 "
          />
        </div>
        <Image
          src="/images/phoneScreen.png"
          alt="phoneScreen"
          width={370}
          height={370}
          className="mt-20"
        />
      </div>
    </div>
  );
}
